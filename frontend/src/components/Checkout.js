import React, { useEffect, useContext } from 'react';
import { CartContext } from '../CartContext';

const Checkout = () => {
    const { cartItems } = useContext(CartContext);

    useEffect(() => {
        const createCheckoutSession = async () => {
            const response = await fetch('http://localhost:8080/api/orders/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ items: cartItems.map(item => ({ productId: item.id, quantity: item.quantity })) }),
            });
            const data = await response.json();
            window.location.href = data.checkoutUrl;
        };

        if (cartItems.length > 0) {
            createCheckoutSession();
        }
    }, [cartItems]);

    return (
        <div>
            <h2>Checkout</h2>
            <p>Redirecting to payment gateway...</p>
        </div>
    );
};

export default Checkout;

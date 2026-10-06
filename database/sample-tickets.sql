USE resolvex;
INSERT INTO tickets (customer_name,email,subject,description,category,priority,status,ai_reply)
VALUES
('Aarav Sharma','aarav@example.com','Payment failed','My payment failed and the amount was deducted. Please help.','PAYMENT','HIGH','OPEN','Hi Aarav, thank you for contacting us regarding your payment issue. Our support team will review the transaction and help resolve it as soon as possible.'),
('Priya Mehta','priya@example.com','Cannot login','I cannot log in to my account and the password reset is not working.','LOGIN','MEDIUM','IN_PROGRESS','Hi Priya, thank you for contacting us regarding your login issue. Our support team will help restore access to your account.')

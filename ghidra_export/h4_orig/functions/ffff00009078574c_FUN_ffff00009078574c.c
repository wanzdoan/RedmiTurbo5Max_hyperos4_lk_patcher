// ffff00009078574c  size=72  FUN_ffff00009078574c

undefined8 FUN_ffff00009078574c(undefined8 param_1,uint param_2)

{
  uint in_w8;
  
  *(uint *)(((ulong)(in_w8 << 1) & 0xfffffff0) - 0xffffeffd2cf4) =
       1 << (ulong)((in_w8 & 7) << 2 | 3) | (param_2 & 7) << (ulong)((in_w8 & 7) << 2);
  return 0;
}



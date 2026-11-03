// File offset entry: 0xaac54

void FUN_020aab54(void)

{
  undefined4 in_w9;
  undefined8 in_x10;
  long *unaff_x20;
  
  *(undefined4 *)(*unaff_x20 + 0x104) = in_w9;
  *(uint *)(*unaff_x20 + 0x104) = (uint)((ulong)in_x10 >> 0x20) & 0xf;
  *(undefined4 *)(*unaff_x20 + 0x104) = 0;
  *(undefined4 *)(*unaff_x20 + 0x104) = 0;
  *(undefined4 *)(*unaff_x20 + 0x104) = 0;
  return;
}




undefined8 FUN_ffff0000b800ecf0(byte *param_1,int *param_2)

{
  uint uVar1;
  
  uVar1 = FUN_ffff0000b8013ae8();
  *param_2 = (int)(short)uVar1;
  *param_1 = (byte)(uVar1 >> 0xf) & 1;
  return 0;
}



// ffff00009071fe08  size=104  FUN_ffff00009071fe08

void FUN_ffff00009071fe08(long param_1)

{
  undefined4 uVar1;
  
  *(undefined4 *)(param_1 + 4) = 0x42414342;
  *(undefined1 *)(param_1 + 8) = 1;
  *(undefined1 *)(param_1 + 0xc) = 0x77;
  *(ushort *)(param_1 + 9) = *(ushort *)(param_1 + 9) & 0xfff8 | 2;
  *(undefined1 *)(param_1 + 0xe) = 0x77;
  uVar1 = FUN_ffff00009076c7c4(0,param_1,0x1c);
  *(undefined4 *)(param_1 + 0x1c) = uVar1;
  return;
}



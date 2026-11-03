// ffff00009071fe00  size=36  FUN_ffff00009071fe00

void FUN_ffff00009071fe00(long param_1)

{
  undefined4 uVar1;
  
  if (param_1 != 0) {
    *(undefined4 *)(param_1 + 4) = 0x42414342;
    *(undefined1 *)(param_1 + 8) = 1;
    *(undefined1 *)(param_1 + 0xc) = 0x77;
    *(ushort *)(param_1 + 9) = *(ushort *)(param_1 + 9) & 0xfff8 | 2;
    *(undefined1 *)(param_1 + 0xe) = 0x77;
    uVar1 = FUN_ffff00009076c7c4(0,param_1,0x1c);
    *(undefined4 *)(param_1 + 0x1c) = uVar1;
    return;
  }
  FUN_ffff00009077de54
            (s__s__d___LK_AB___s_check_args_fai_ffff0000907f5ef6,
             s_initDefaultBootControl_ffff0000907ef453,0x75,
             s_initDefaultBootControl_ffff0000907ef453);
  return;
}



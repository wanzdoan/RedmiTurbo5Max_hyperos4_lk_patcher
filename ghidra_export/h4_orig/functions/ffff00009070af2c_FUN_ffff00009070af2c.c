// ffff00009070af2c  size=316  FUN_ffff00009070af2c

undefined8 FUN_ffff00009070af2c(void)

{
  uint uVar1;
  char *pcVar2;
  long lVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  pcVar2 = (char *)FUN_ffff000090741bdc(s_MTK_DEVICE_ID_ffff0000907fe9e7);
  lVar3 = FUN_ffff00009077f250(0xffff00009083d2ac);
  if (lVar3 == 0) {
    if (pcVar2 == (char *)0x0) {
      FUN_ffff00009077e11c(s_Set_serial___to_default_value__ffff0000907ebf12);
      pcVar2 = s_0123456789ABCDEF_ffff0000907fb090;
      uVar5 = FUN_ffff00009077f250(s_0123456789ABCDEF_ffff0000907fb090);
      uVar5 = uVar5 & 0xffffffff;
    }
    else {
      FUN_ffff00009077e11c(s_Set_serial___from_para__ffff0000907da527);
      uVar1 = FUN_ffff00009077f250(pcVar2);
      if (0x12 < uVar1) {
        uVar1 = 0x13;
      }
      uVar5 = (ulong)uVar1;
    }
    FUN_ffff00009077f2ac(0xffff00009083d2ac,pcVar2,uVar5);
    *(undefined1 *)(uVar5 - 0xffff6f7c2d54) = 0;
    lVar3 = thunk_FUN_ffff00009074b22c(0xd);
    uVar5 = thunk_FUN_ffff00009074b22c(0xc);
    uVar5 = uVar5 & 0xffffffff | lVar3 << 0x20;
    if (uVar5 == 0) {
      uVar4 = FUN_ffff00009077f250(s_0123456789ABCDEF_ffff0000907fb090);
      FUN_ffff00009077eb20(0xffff00009083d2ac,s_0123456789ABCDEF_ffff0000907fb090,uVar4);
      uVar4 = 2;
    }
    else {
      FUN_ffff00009070ad3c(uVar5,0x50f7f50c,0xffff00009083d2ac);
      uVar4 = 1;
    }
    FUN_ffff00009077e11c(s_Set_serial___from_efuse__error____ffff0000907f25cb,uVar4);
    uVar1 = FUN_ffff00009077f250(0xffff00009083d2ac);
    if (0x12 < uVar1) {
      uVar1 = 0x13;
    }
    *(undefined1 *)((ulong)uVar1 - 0xffff6f7c2d54) = 0;
  }
  return 0xffff00009083d2ac;
}



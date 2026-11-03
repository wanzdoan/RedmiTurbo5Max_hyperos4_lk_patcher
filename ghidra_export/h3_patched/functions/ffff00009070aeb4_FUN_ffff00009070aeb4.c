// ffff00009070aeb4  size=316  FUN_ffff00009070aeb4

undefined8 FUN_ffff00009070aeb4(void)

{
  uint uVar1;
  char *pcVar2;
  long lVar3;
  undefined8 uVar4;
  ulong uVar5;
  
  pcVar2 = (char *)FUN_ffff000090741944(s_MTK_DEVICE_ID_ffff0000907fe83f);
  lVar3 = FUN_ffff00009077ef88(0xffff00009083d270);
  if (lVar3 == 0) {
    if (pcVar2 == (char *)0x0) {
      FUN_ffff00009077de54(s_Set_serial___to_default_value__ffff0000907ebd78);
      pcVar2 = s_0123456789ABCDEF_ffff0000907faef4;
      uVar5 = FUN_ffff00009077ef88(s_0123456789ABCDEF_ffff0000907faef4);
      uVar5 = uVar5 & 0xffffffff;
    }
    else {
      FUN_ffff00009077de54(s_Set_serial___from_para__ffff0000907da479);
      uVar1 = FUN_ffff00009077ef88(pcVar2);
      if (0x12 < uVar1) {
        uVar1 = 0x13;
      }
      uVar5 = (ulong)uVar1;
    }
    FUN_ffff00009077efe4(0xffff00009083d270,pcVar2,uVar5);
    *(undefined1 *)(uVar5 - 0xffff6f7c2d90) = 0;
    lVar3 = thunk_FUN_ffff00009074af94(0xd);
    uVar5 = thunk_FUN_ffff00009074af94(0xc);
    uVar5 = uVar5 & 0xffffffff | lVar3 << 0x20;
    if (uVar5 == 0) {
      uVar4 = FUN_ffff00009077ef88(s_0123456789ABCDEF_ffff0000907faef4);
      FUN_ffff00009077e858(0xffff00009083d270,s_0123456789ABCDEF_ffff0000907faef4,uVar4);
      uVar4 = 2;
    }
    else {
      FUN_ffff00009070acc4(uVar5,0x50f7f50c,0xffff00009083d270);
      uVar4 = 1;
    }
    FUN_ffff00009077de54(s_Set_serial___from_efuse__error____ffff0000907f2428,uVar4);
    uVar1 = FUN_ffff00009077ef88(0xffff00009083d270);
    if (0x12 < uVar1) {
      uVar1 = 0x13;
    }
    *(undefined1 *)((ulong)uVar1 - 0xffff6f7c2d90) = 0;
  }
  return 0xffff00009083d270;
}



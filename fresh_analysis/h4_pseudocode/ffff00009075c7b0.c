
void FUN_ffff00009075c7b0(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined8 uVar4;
  
  if ((DAT_ffff00009087bf20 & 1) != 0) {
    uVar3 = FUN_ffff000090781c0c();
    if (((uVar3 & 1) == 0) ||
       (iVar1 = FUN_ffff00009078395c(), uVar4 = DAT_ffff00009087bf28, iVar1 != 0xfc11)) {
      uVar4 = DAT_ffff00009087bf28;
      uVar2 = FUN_ffff00009075e264();
    }
    else {
      uVar2 = FUN_ffff00009075e3d0();
    }
    FUN_ffff000090707c50(uVar4,uVar2);
    FUN_ffff00009075da38(1);
    return;
  }
  FUN_ffff00009077e11c(s_disp_has_no_framebuffer__skip_up_ffff0000907db4c3);
  return;
}



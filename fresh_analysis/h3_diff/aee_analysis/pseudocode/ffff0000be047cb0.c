
void FUN_ffff0000be047cb0(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined8 uVar4;
  
  if ((DAT_ffff0000be126068 & 1) != 0) {
    uVar3 = FUN_ffff0000be063c7c();
    if (((uVar3 & 1) == 0) ||
       (iVar1 = FUN_ffff0000be065774(), uVar4 = DAT_ffff0000be126070, iVar1 != 0xfc11)) {
      uVar4 = DAT_ffff0000be126070;
      uVar2 = FUN_ffff0000be048f80();
    }
    else {
      uVar2 = FUN_ffff0000be0490ec();
    }
    FUN_ffff0000be007308(uVar4,uVar2);
    FUN_ffff0000be048894(1);
    return;
  }
  FUN_ffff0000be05fd98(s_disp_has_no_framebuffer__skip_up_ffff0000be0abc10);
  return;
}



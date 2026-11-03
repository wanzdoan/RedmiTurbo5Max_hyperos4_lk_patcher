
void FUN_ffff0000b803e8ec(void)

{
  int iVar1;
  undefined4 uVar2;
  ulong uVar3;
  undefined8 uVar4;
  
  if ((DAT_ffff0000b8129868 & 1) != 0) {
    uVar3 = FUN_ffff0000b804f554();
    if (((uVar3 & 1) == 0) ||
       (iVar1 = FUN_ffff0000b8050384(), uVar4 = DAT_ffff0000b8129870, iVar1 != 0xfc11)) {
      uVar4 = DAT_ffff0000b8129870;
      uVar2 = FUN_ffff0000b803fd20();
    }
    else {
      uVar2 = FUN_ffff0000b803fe8c();
    }
    FUN_ffff0000b800758c(uVar4,uVar2);
    FUN_ffff0000b803f4dc(1);
    return;
  }
  FUN_ffff0000b804c33c(s_disp_has_no_framebuffer__skip_up_ffff0000b808bd6e);
  return;
}



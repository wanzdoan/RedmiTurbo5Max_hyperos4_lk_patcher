
void FUN_ffff0000b803fd20(void)

{
  long *plVar1;
  uint uVar2;
  uint uVar3;
  
  if (DAT_ffff0000b8129894 != 0) {
    return;
  }
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  if (DAT_ffff0000b81298b0 == (long *)0x0) {
    plVar1 = (long *)FUN_ffff0000b803d960(0,0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    DAT_ffff0000b81298b0 = plVar1;
    if (plVar1 != (long *)0x0) goto LAB_ffff0000b803fdb0;
    plVar1 = (long *)FUN_ffff0000b803d960(0,0);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    uVar2 = 0;
    uVar3 = 0;
    DAT_ffff0000b81298b0 = plVar1;
    if (plVar1 == (long *)0x0) goto LAB_ffff0000b803fde4;
  }
  else {
LAB_ffff0000b803fdb0:
    if (*DAT_ffff0000b81298b0 == 0) {
      uVar3 = 0;
      uVar2 = 0;
      goto LAB_ffff0000b803fde4;
    }
    uVar2 = *(int *)(*DAT_ffff0000b81298b0 + 0x18) * 0x10 + 0x1f0U & 0xfffffe00;
  }
  uVar3 = 0;
  if (*DAT_ffff0000b81298b0 != 0) {
    uVar3 = *(int *)(*DAT_ffff0000b81298b0 + 0x1c) + 0x1fU & 0xffffffe0;
  }
LAB_ffff0000b803fde4:
  DAT_ffff0000b8129894 = uVar2 * uVar3 & 0x7fffc000;
  return;
}



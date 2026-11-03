
undefined8 FUN_ffff0000be035434(void)

{
  uint uVar1;
  long lVar2;
  int iVar3;
  undefined4 uVar4;
  uint uVar5;
  long lVar6;
  ulong uVar7;
  int iVar8;
  long *plVar9;
  
  plVar9 = DAT_ffff0000be121850;
  if (DAT_ffff0000be121850 == (long *)0x0) {
    DAT_ffff0000be121850 = (long *)FUN_ffff0000be05f1bc(0x20);
    if (DAT_ffff0000be121850 == (long *)0x0) {
LAB_ffff0000be035554:
      DAT_ffff0000be121818 = (long *)0x0;
      FUN_ffff0000be05fd98(s_video_hw_init_failed__check_disp_ffff0000be0a4ac7);
      return 0xffffffff;
    }
    FUN_ffff0000be060ae0(DAT_ffff0000be121850,0,0x20);
    lVar6 = FUN_ffff0000be047c5c();
    uVar7 = FUN_ffff0000be047c70();
    *DAT_ffff0000be121850 = lVar6 + (uVar7 & 0xffffffff);
    uVar4 = thunk_FUN_ffff0000be0485c4();
    *(undefined4 *)((long)DAT_ffff0000be121850 + 0x14) = uVar4;
    uVar4 = FUN_ffff0000be047d3c();
    plVar9 = DAT_ffff0000be121850;
    *(undefined4 *)(DAT_ffff0000be121850 + 3) = uVar4;
    *(undefined4 *)((long)plVar9 + 0xc) = 3;
    uVar5 = thunk_FUN_ffff0000be048884();
    plVar9 = DAT_ffff0000be121850;
    uVar1 = uVar5 + 7;
    if (-1 < (int)uVar5) {
      uVar1 = uVar5;
    }
    iVar3 = *(int *)((long)DAT_ffff0000be121850 + 0x14);
    lVar2 = DAT_ffff0000be121850[3];
    *(int *)(DAT_ffff0000be121850 + 2) = (int)uVar1 >> 3;
    *(int *)(plVar9 + 1) = iVar3 * ((int)uVar1 >> 3) * (int)lVar2;
    if ((((lVar6 == 0) || (iVar3 == 0)) || ((int)lVar2 == 0)) || (uVar5 + 7 < 0xf)) {
      FUN_ffff0000be05fd98(s_gd_fb_addr_0x_08lx___wxh____dx_d_ffff0000be0be558,*plVar9);
      thunk_FUN_ffff0000be09937c(DAT_ffff0000be121850);
      DAT_ffff0000be121850 = (long *)0x0;
      goto LAB_ffff0000be035554;
    }
  }
  DAT_ffff0000be121818 = plVar9;
  iVar3 = FUN_ffff0000be060f54(&DAT_ffff0000be0ae340,&DAT_ffff0000be0aa587,2);
  if (iVar3 == 0) {
    iVar8 = 3;
    iVar3 = 4;
  }
  else {
    iVar3 = FUN_ffff0000be060f54(&DAT_ffff0000be0ae340,&DAT_ffff0000be0bc0e4,3);
    if (iVar3 == 0) {
      iVar3 = 4;
      iVar8 = 3;
    }
    else {
      iVar8 = 4;
      iVar3 = 3;
    }
  }
  uVar1 = *(uint *)(plVar9 + 3) >> iVar8;
  uVar5 = *(uint *)((long)plVar9 + 0x14) >> iVar3;
  switch(*(undefined4 *)((long)plVar9 + 0xc)) {
  case 0:
    DAT_ffff0000be121824 = 0x1010101;
    break;
  case 1:
    DAT_ffff0000be121824 = 0x52945294;
    break;
  case 2:
    DAT_ffff0000be121824 = 0xa514a514;
    break;
  case 3:
    DAT_ffff0000be121824 = 0xa0a0a0;
    break;
  case 4:
    DAT_ffff0000be121824 = 0xa0a0a0a0;
    break;
  case 5:
    DAT_ffff0000be121824 = 0xb6b6b6b6;
    break;
  default:
    goto switchD_ffff0000be0355c0_default;
  }
  DAT_ffff0000be121828 = 0;
switchD_ffff0000be0355c0_default:
  DAT_ffff0000be121800 = 0;
  DAT_ffff0000be121820 = 0;
  if (uVar1 != 0) {
    DAT_ffff0000be121820 = uVar1 - 1;
    DAT_ffff0000be121800 = uVar5;
  }
  DAT_ffff0000be121808 = (ulong)uVar1;
  DAT_ffff0000be121810 = (ulong)uVar5;
  DAT_ffff0000be12182c = DAT_ffff0000be121824 ^ DAT_ffff0000be121828;
  DAT_ffff0000be121830 = DAT_ffff0000be121824 ^ DAT_ffff0000be121828;
  DAT_ffff0000be121834 = DAT_ffff0000be121824;
  DAT_ffff0000be121838 = DAT_ffff0000be121828;
  DAT_ffff0000be121840 = *plVar9;
  return 0;
}



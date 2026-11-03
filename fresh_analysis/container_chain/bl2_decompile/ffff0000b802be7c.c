
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b802be7c(void)

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
  
  plVar9 = DAT_ffff0000b8125050;
  if (DAT_ffff0000b8125050 == (long *)0x0) {
    DAT_ffff0000b8125050 = (long *)FUN_ffff0000b804bccc(0x20);
    if (DAT_ffff0000b8125050 == (long *)0x0) {
LAB_ffff0000b802bf9c:
      DAT_ffff0000b8125020 = (long *)0x0;
      FUN_ffff0000b804c33c(s_video_hw_init_failed__check_disp_ffff0000b80865da);
      return 0xffffffff;
    }
    FUN_ffff0000b804d070(DAT_ffff0000b8125050,0,0x20);
    lVar6 = FUN_ffff0000b803e898();
    uVar7 = FUN_ffff0000b803e8ac();
    *DAT_ffff0000b8125050 = lVar6 + (uVar7 & 0xffffffff);
    uVar4 = thunk_FUN_ffff0000b803f20c();
    *(undefined4 *)((long)DAT_ffff0000b8125050 + 0x14) = uVar4;
    uVar4 = FUN_ffff0000b803e984();
    plVar9 = DAT_ffff0000b8125050;
    *(undefined4 *)(DAT_ffff0000b8125050 + 3) = uVar4;
    *(undefined4 *)((long)plVar9 + 0xc) = 3;
    uVar5 = thunk_FUN_ffff0000b803f4cc();
    plVar9 = DAT_ffff0000b8125050;
    uVar1 = uVar5 + 7;
    if (-1 < (int)uVar5) {
      uVar1 = uVar5;
    }
    iVar3 = *(int *)((long)DAT_ffff0000b8125050 + 0x14);
    lVar2 = DAT_ffff0000b8125050[3];
    *(int *)(DAT_ffff0000b8125050 + 2) = (int)uVar1 >> 3;
    *(int *)(plVar9 + 1) = iVar3 * ((int)uVar1 >> 3) * (int)lVar2;
    if ((((lVar6 == 0) || (iVar3 == 0)) || ((int)lVar2 == 0)) || (uVar5 + 7 < 0xf)) {
      FUN_ffff0000b804c33c(s_gd_fb_addr_0x_08lx___wxh____dx_d_ffff0000b809aadd,*plVar9);
      thunk_FUN_ffff0000b8082558(DAT_ffff0000b8125050);
      DAT_ffff0000b8125050 = (long *)0x0;
      goto LAB_ffff0000b802bf9c;
    }
  }
  DAT_ffff0000b8125020 = plVar9;
  iVar3 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b808ab6e,2);
  if (iVar3 == 0) {
    iVar8 = 3;
    iVar3 = 4;
  }
  else {
    iVar3 = FUN_ffff0000b804d4c4(&DAT_ffff0000b808dbfe,&DAT_ffff0000b8098f2a,3);
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
    DAT_ffff0000b812502c = 0x1010101;
    break;
  case 1:
    DAT_ffff0000b812502c = 0x52945294;
    break;
  case 2:
    DAT_ffff0000b812502c = 0xa514a514;
    break;
  case 3:
    DAT_ffff0000b812502c = 0xa0a0a0;
    break;
  case 4:
    DAT_ffff0000b812502c = 0xa0a0a0a0;
    break;
  case 5:
    DAT_ffff0000b812502c = 0xb6b6b6b6;
    break;
  default:
    goto switchD_ffff0000b802c008_default;
  }
  DAT_ffff0000b8125030 = 0;
switchD_ffff0000b802c008_default:
  DAT_ffff0000b8125008 = 0;
  DAT_ffff0000b8125028 = 0;
  if (uVar1 != 0) {
    DAT_ffff0000b8125028 = uVar1 - 1;
    DAT_ffff0000b8125008 = uVar5;
  }
  _DAT_ffff0000b8125010 = (ulong)uVar1;
  _DAT_ffff0000b8125018 = (ulong)uVar5;
  DAT_ffff0000b8125034 = DAT_ffff0000b812502c ^ DAT_ffff0000b8125030;
  DAT_ffff0000b8125038 = DAT_ffff0000b812502c ^ DAT_ffff0000b8125030;
  DAT_ffff0000b812503c = DAT_ffff0000b812502c;
  DAT_ffff0000b8125040 = DAT_ffff0000b8125030;
  DAT_ffff0000b8125048 = *plVar9;
  return 0;
}



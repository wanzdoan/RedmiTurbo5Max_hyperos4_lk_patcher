
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090745d58(void)

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
  
  plVar9 = DAT_ffff0000908775c0;
  if (DAT_ffff0000908775c0 == (long *)0x0) {
    DAT_ffff0000908775c0 = (long *)FUN_ffff00009077d514(0x20);
    if (DAT_ffff0000908775c0 == (long *)0x0) {
LAB_ffff000090745e78:
      DAT_ffff000090877590 = (long *)0x0;
      FUN_ffff00009077e11c(s_video_hw_init_failed__check_disp_ffff0000907d1524);
      return 0xffffffff;
    }
    FUN_ffff00009077ee50(DAT_ffff0000908775c0,0,0x20);
    lVar6 = FUN_ffff00009075c608();
    uVar7 = FUN_ffff00009075c61c();
    *DAT_ffff0000908775c0 = lVar6 + (uVar7 & 0xffffffff);
    uVar4 = thunk_FUN_ffff00009075d768();
    *(undefined4 *)((long)DAT_ffff0000908775c0 + 0x14) = uVar4;
    uVar4 = FUN_ffff00009075c8fc();
    plVar9 = DAT_ffff0000908775c0;
    *(undefined4 *)(DAT_ffff0000908775c0 + 3) = uVar4;
    *(undefined4 *)((long)plVar9 + 0xc) = 3;
    uVar5 = thunk_FUN_ffff00009075da28();
    plVar9 = DAT_ffff0000908775c0;
    uVar1 = uVar5 + 7;
    if (-1 < (int)uVar5) {
      uVar1 = uVar5;
    }
    iVar3 = *(int *)((long)DAT_ffff0000908775c0 + 0x14);
    lVar2 = DAT_ffff0000908775c0[3];
    *(int *)(DAT_ffff0000908775c0 + 2) = (int)uVar1 >> 3;
    *(int *)(plVar9 + 1) = iVar3 * ((int)uVar1 >> 3) * (int)lVar2;
    if ((((lVar6 == 0) || (iVar3 == 0)) || ((int)lVar2 == 0)) || (uVar5 + 7 < 0xf)) {
      FUN_ffff00009077e11c(s_gd_fb_addr_0x_08lx___wxh____dx_d_ffff0000907f675b,*plVar9);
      thunk_FUN_ffff0000907c314c(DAT_ffff0000908775c0);
      DAT_ffff0000908775c0 = (long *)0x0;
      goto LAB_ffff000090745e78;
    }
  }
  DAT_ffff000090877590 = plVar9;
  iVar3 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907d9514,2);
  if (iVar3 == 0) {
    iVar8 = 3;
    iVar3 = 4;
  }
  else {
    iVar3 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f2fb1,3);
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
    DAT_ffff00009087759c = 0x1010101;
    break;
  case 1:
    DAT_ffff00009087759c = 0x52945294;
    break;
  case 2:
    DAT_ffff00009087759c = 0xa514a514;
    break;
  case 3:
    DAT_ffff00009087759c = 0xa0a0a0;
    break;
  case 4:
    DAT_ffff00009087759c = 0xa0a0a0a0;
    break;
  case 5:
    DAT_ffff00009087759c = 0xb6b6b6b6;
    break;
  default:
    goto switchD_ffff000090745ee4_default;
  }
  DAT_ffff0000908775a0 = 0;
switchD_ffff000090745ee4_default:
  DAT_ffff000090877578 = 0;
  DAT_ffff000090877598 = 0;
  if (uVar1 != 0) {
    DAT_ffff000090877598 = uVar1 - 1;
    DAT_ffff000090877578 = uVar5;
  }
  DAT_ffff000090877580 = (ulong)uVar1;
  _DAT_ffff000090877588 = (ulong)uVar5;
  DAT_ffff0000908775a4 = DAT_ffff00009087759c ^ DAT_ffff0000908775a0;
  DAT_ffff0000908775a8 = DAT_ffff00009087759c ^ DAT_ffff0000908775a0;
  DAT_ffff0000908775ac = DAT_ffff00009087759c;
  DAT_ffff0000908775b0 = DAT_ffff0000908775a0;
  DAT_ffff0000908775b8 = *plVar9;
  return 0;
}



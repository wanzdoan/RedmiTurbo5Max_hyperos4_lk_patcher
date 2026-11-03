// ffff000090745ac0  size=640  FUN_ffff000090745ac0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090745ac0(void)

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
  
  plVar9 = _DAT_ffff000090877528;
  if (_DAT_ffff000090877528 == (long *)0x0) {
    _DAT_ffff000090877528 = (long *)FUN_ffff00009077d24c(0x20);
    if (_DAT_ffff000090877528 == (long *)0x0) {
LAB_ffff000090745be0:
      _DAT_ffff0000908774f8 = (long *)0x0;
      FUN_ffff00009077de54(s_video_hw_init_failed__check_disp_ffff0000907d14e7);
      return 0xffffffff;
    }
    FUN_ffff00009077eb88(_DAT_ffff000090877528,0,0x20);
    lVar6 = FUN_ffff00009075c370();
    uVar7 = FUN_ffff00009075c384();
    *_DAT_ffff000090877528 = lVar6 + (uVar7 & 0xffffffff);
    uVar4 = thunk_FUN_ffff00009075d4d0();
    *(undefined4 *)((long)_DAT_ffff000090877528 + 0x14) = uVar4;
    uVar4 = FUN_ffff00009075c664();
    plVar9 = _DAT_ffff000090877528;
    *(undefined4 *)(_DAT_ffff000090877528 + 3) = uVar4;
    *(undefined4 *)((long)plVar9 + 0xc) = 3;
    uVar5 = thunk_FUN_ffff00009075d790();
    plVar9 = _DAT_ffff000090877528;
    uVar1 = uVar5 + 7;
    if (-1 < (int)uVar5) {
      uVar1 = uVar5;
    }
    iVar3 = *(int *)((long)_DAT_ffff000090877528 + 0x14);
    lVar2 = _DAT_ffff000090877528[3];
    *(int *)(_DAT_ffff000090877528 + 2) = (int)uVar1 >> 3;
    *(int *)(plVar9 + 1) = iVar3 * ((int)uVar1 >> 3) * (int)lVar2;
    if ((((lVar6 == 0) || (iVar3 == 0)) || ((int)lVar2 == 0)) || (uVar5 + 7 < 0xf)) {
      FUN_ffff00009077de54(s_gd_fb_addr_0x_08lx___wxh____dx_d_ffff0000907f65d1,*plVar9);
      thunk_FUN_ffff0000907c2ad8(_DAT_ffff000090877528);
      _DAT_ffff000090877528 = (long *)0x0;
      goto LAB_ffff000090745be0;
    }
  }
  _DAT_ffff0000908774f8 = plVar9;
  iVar3 = FUN_ffff00009077f080(&DAT_ffff0000907df295,&DAT_ffff0000907d947d,2);
  if (iVar3 == 0) {
    iVar8 = 3;
    iVar3 = 4;
  }
  else {
    iVar3 = FUN_ffff00009077f080(&DAT_ffff0000907df295,&DAT_ffff0000907f2e25,3);
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
    _DAT_ffff000090877504 = 0x1010101;
    break;
  case 1:
    _DAT_ffff000090877504 = 0x52945294;
    break;
  case 2:
    _DAT_ffff000090877504 = 0xa514a514;
    break;
  case 3:
    _DAT_ffff000090877504 = 0xa0a0a0;
    break;
  case 4:
    _DAT_ffff000090877504 = 0xa0a0a0a0;
    break;
  case 5:
    _DAT_ffff000090877504 = 0xb6b6b6b6;
    break;
  default:
    goto switchD_ffff000090745c4c_default;
  }
  _DAT_ffff000090877508 = 0;
switchD_ffff000090745c4c_default:
  _DAT_ffff0000908774e0 = 0;
  _DAT_ffff000090877500 = 0;
  if (uVar1 != 0) {
    _DAT_ffff000090877500 = uVar1 - 1;
    _DAT_ffff0000908774e0 = uVar5;
  }
  _DAT_ffff0000908774e8 = (ulong)uVar1;
  _DAT_ffff0000908774f0 = (ulong)uVar5;
  _DAT_ffff00009087750c = _DAT_ffff000090877504 ^ _DAT_ffff000090877508;
  _DAT_ffff000090877510 = _DAT_ffff000090877504 ^ _DAT_ffff000090877508;
  _DAT_ffff000090877514 = _DAT_ffff000090877504;
  _DAT_ffff000090877518 = _DAT_ffff000090877508;
  _DAT_ffff000090877520 = *plVar9;
  return 0;
}



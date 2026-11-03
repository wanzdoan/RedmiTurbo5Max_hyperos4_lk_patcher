// ffff00009078b7a8  size=1268  FUN_ffff00009078b7a8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078b7a8(void)

{
  uint *puVar1;
  long lVar2;
  long lVar3;
  undefined8 *puVar4;
  bool bVar5;
  int iVar6;
  undefined8 *puVar7;
  uint uVar8;
  char *pcVar9;
  
  lVar3 = _DAT_ffff00009088a028;
  lVar2 = _DAT_ffff00009088a020;
  if (_DAT_ffff00009088a020 == 0) {
    FUN_ffff00009077de54(s__USB__udc_cannot_start_before_in_ffff0000907fe1b7);
    return 0xffffffed;
  }
  if (_DAT_ffff00009088a028 == 0) {
    FUN_ffff00009077de54(s__USB__udc_has_no_gadget_register_ffff0000907eed97);
    return 0xfffffffa;
  }
  FUN_ffff00009078bd34(_DAT_ffff00009088a020,0);
  FUN_ffff00009078be74(lVar3,0);
  puVar7 = (undefined8 *)FUN_ffff00009077d24c(0x14);
  if (puVar7 == (undefined8 *)0x0) {
    FUN_ffff00009077de54(s__USB__alloc_desc_failed__type__d_ffff0000907e3ad6,3);
  }
  else {
    FUN_ffff00009077eb88(puVar7,0,0x14);
    puVar4 = puVar7;
    *puVar7 = _DAT_ffff00009088a168;
    _DAT_ffff00009088a168 = puVar4;
    puVar7[1] = 0x409030400040300;
  }
  if ((DAT_ffff00009088a185 >> 1 & 1) != 0) {
    FUN_ffff00009078bd34(lVar2,1);
    puVar7 = (undefined8 *)FUN_ffff00009077d24c(0x26);
    if (puVar7 == (undefined8 *)0x0) {
      FUN_ffff00009077de54(s__USB__alloc_desc_failed__type__d_ffff0000907e3ad6,0xf);
    }
    else {
      FUN_ffff00009077eb88(puVar7,0,0x26);
      *(undefined4 *)(puVar7 + 1) = 0x160f00;
      *(undefined2 *)((long)puVar7 + 0xc) = 0xf05;
      puVar4 = _DAT_ffff00009088a178;
      _DAT_ffff00009088a178 = puVar7;
      *(undefined8 *)((long)puVar7 + 0xe) = 0x2021007020016;
      *(undefined8 *)((long)puVar7 + 0x16) = 0xe0003100a0000;
      *(undefined4 *)((long)puVar7 + 0x1e) = 0x200a01;
      *puVar7 = puVar4;
    }
    FUN_ffff00009078be74(lVar3,1);
    puVar7 = (undefined8 *)FUN_ffff00009077d24c(0x14);
    if (puVar7 == (undefined8 *)0x0) {
      FUN_ffff00009077de54(s__USB__alloc_desc_failed__type__d_ffff0000907e3ad6,3);
    }
    else {
      FUN_ffff00009077eb88(puVar7,0,0x14);
      puVar4 = puVar7;
      *puVar7 = _DAT_ffff00009088a178;
      _DAT_ffff00009088a178 = puVar4;
      puVar7[1] = 0x409030400040300;
    }
  }
  FUN_ffff00009078e0a8();
  _DAT_ffff000016703e98 = _DAT_ffff000016703e98 | 1;
  FUN_ffff00009077cbd8(1);
  _DAT_ffff000016703e98 = _DAT_ffff000016703e98 & 0xfffffffe;
  _DAT_ffff000016703e00 = _DAT_ffff000016703e00 & 0xfffffffe;
  _DAT_ffff000016703e08 = _DAT_ffff000016703e08 & 0xfffffffe;
  _DAT_ffff000016703e50 = _DAT_ffff000016703e50 & 0xfffffff8;
  uVar8 = 0x501;
  if ((DAT_ffff00009088a185 >> 1 & 1) != 0) {
    _DAT_ffff000016703e30 = _DAT_ffff000016703e30 & 0xfffffff8;
    uVar8 = 0x501;
    if ((DAT_ffff00009088a185 & 2) != 0) {
      uVar8 = 0x10501;
    }
  }
  iVar6 = 100;
  pcVar9 = s__USB__SSUSB_SYS125_RST_B_STS_NG_ffff0000907d1f74;
  do {
    if ((uVar8 & (_DAT_ffff000016703e10 ^ 0xffffffff)) == 0) {
      iVar6 = 100;
      pcVar9 = s__USB__SSUSB_U2_MAC_SYS_RST_B_STS_ffff0000907f8d7c;
      goto LAB_ffff00009078ba14;
    }
    FUN_ffff00009077cbd8(100);
    iVar6 = iVar6 + -1;
  } while (iVar6 != 0);
  goto LAB_ffff00009078ba2c;
  while( true ) {
    FUN_ffff00009077cbd8(100);
    iVar6 = iVar6 + -1;
    if (iVar6 == 0) break;
LAB_ffff00009078ba14:
    if ((_DAT_ffff000016703e14 & 1) != 0) goto LAB_ffff00009078ba40;
  }
LAB_ffff00009078ba2c:
  FUN_ffff00009077de54(pcVar9);
  FUN_ffff00009077de54(s__USB__Refer_clocks_stability_che_ffff0000907ea05e);
LAB_ffff00009078ba40:
  lVar2 = _DAT_ffff00009088a120;
  _DAT_ffff000016701080 = 0xffffffff;
  _DAT_ffff000016701700 = 0xffffffff;
  _DAT_ffff000016701710 = 0xffffffff;
  _DAT_ffff000016701780 = 0xffffffff;
  _DAT_ffff0000167017c0 = 0xffffffff;
  _DAT_ffff0000167017d0 = 0xffffffff;
  _DAT_ffff00001670100c = 0xffffffff;
  _DAT_ffff00001670108c = 0xffffffff;
  _DAT_ffff00001670170c = 0xffffffff;
  _DAT_ffff00001670171c = 0xffffffff;
  _DAT_ffff00001670178c = 0xffffffff;
  _DAT_ffff0000167017cc = 0xffffffff;
  _DAT_ffff0000167017dc = 0xffffffff;
  _DAT_ffff00001670341c = 0xffffffff;
  if ((DAT_ffff00009088a185 >> 1 & 1) != 0) {
    _DAT_ffff000016702610 = _DAT_ffff000016702610 & 0xfffffcfc;
    _DAT_ffff000016702410 = _DAT_ffff000016702410 & 0xfffffffb;
    _DAT_ffff000016703414 = _DAT_ffff000016703414 | 0x400;
  }
  _DAT_ffff000016703424 = _DAT_ffff000016703424 & 0xffff;
  _DAT_ffff000016701800 = 0;
  _DAT_ffff000016701400 = _DAT_ffff000016701400 | 1;
  _DAT_ffff000016701c84 = _DAT_ffff000016701c84 | 3;
  if (_DAT_ffff00009088a034 == 2) {
    _DAT_ffff00001670241c = _DAT_ffff00001670241c & 0xfffffffe;
    _DAT_ffff000016703404 = _DAT_ffff000016703404 | 0x20;
  }
  else if (_DAT_ffff00009088a034 == 1) {
    _DAT_ffff00001670241c = _DAT_ffff00001670241c & 0xfffffffe;
    _DAT_ffff000016703404 = _DAT_ffff000016703404 & 0xffffffdf;
  }
  else {
    _DAT_ffff000016703e30 = _DAT_ffff000016703e30 & 0xfffffdff;
  }
  _DAT_ffff000016701008 = 0x37;
  _DAT_ffff000016703418 = 7;
  if ((DAT_ffff00009088a185 >> 1 & 1) != 0) {
    _DAT_ffff00001670253c = 0x1a430;
  }
  _DAT_ffff000016701718 = 0x160006;
  _DAT_ffff000016701850 = 1;
  iVar6 = FUN_ffff00009077f38c(_DAT_ffff00009088a120 + 0x30,&DAT_ffff0000907e3af9);
  if (iVar6 < 0) {
    FUN_ffff00009077de54(s__USB__ep0_>name__copy_failed_ffff0000907ed284);
  }
  *(undefined1 *)(lVar2 + 0x42) = 0;
  *(undefined1 *)(lVar2 + 0x40) = 0;
  _DAT_ffff000016701100 = 0x200;
  if (_DAT_ffff00009088a034 != 3) {
    _DAT_ffff000016701100 = 0x40;
  }
  *(undefined4 *)(lVar2 + 0x3c) = _DAT_ffff000016701100;
  _DAT_ffff000016701088 = _DAT_ffff000016701088 | 1;
  FUN_ffff0000907272e4(0x277,1);
  FUN_ffff0000907272d8(0x277,0);
  FUN_ffff000090715fe4(0x277,FUN_ffff00009078c1bc,0);
  FUN_ffff000090716408(0x277);
  bVar5 = (2 < _DAT_ffff00009088a034 & DAT_ffff00009088a185 >> 1) == 0;
  puVar1 = (uint *)&DAT_ffff00001670241c;
  if (bVar5) {
    puVar1 = (uint *)&DAT_ffff000016703404;
  }
  uVar8 = 0x41;
  if (!bVar5) {
    uVar8 = 1;
  }
  pcVar9 = s__USB__U3_pullup_D_s_ffff0000907fa810;
  if (bVar5) {
    pcVar9 = s__USB__U2_pullup_D_s_ffff0000907db8e0;
  }
  *puVar1 = uVar8 | *puVar1;
  FUN_ffff00009077de54(pcVar9,&DAT_ffff0000907eedbb);
  DAT_ffff00009088a018 = 1;
  return 0;
}



// ffff00009078ba68  size=1268  FUN_ffff00009078ba68

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009078ba68(void)

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
  
  lVar3 = _DAT_ffff00009088a0c0;
  lVar2 = _DAT_ffff00009088a0b8;
  if (_DAT_ffff00009088a0b8 == 0) {
    FUN_ffff00009077e11c(s__USB__udc_cannot_start_before_in_ffff0000907fe35f);
    return 0xffffffed;
  }
  if (_DAT_ffff00009088a0c0 == 0) {
    FUN_ffff00009077e11c(s__USB__udc_has_no_gadget_register_ffff0000907eef08);
    return 0xfffffffa;
  }
  FUN_ffff00009078bff4(_DAT_ffff00009088a0b8,0);
  FUN_ffff00009078c134(lVar3,0);
  puVar7 = (undefined8 *)FUN_ffff00009077d514(0x14);
  if (puVar7 == (undefined8 *)0x0) {
    FUN_ffff00009077e11c(s__USB__alloc_desc_failed__type__d_ffff0000907e3ba7,3);
  }
  else {
    FUN_ffff00009077ee50(puVar7,0,0x14);
    puVar4 = puVar7;
    *puVar7 = _DAT_ffff00009088a200;
    _DAT_ffff00009088a200 = puVar4;
    puVar7[1] = 0x409030400040300;
  }
  if ((DAT_ffff00009088a21d >> 1 & 1) != 0) {
    FUN_ffff00009078bff4(lVar2,1);
    puVar7 = (undefined8 *)FUN_ffff00009077d514(0x26);
    if (puVar7 == (undefined8 *)0x0) {
      FUN_ffff00009077e11c(s__USB__alloc_desc_failed__type__d_ffff0000907e3ba7,0xf);
    }
    else {
      FUN_ffff00009077ee50(puVar7,0,0x26);
      *(undefined4 *)(puVar7 + 1) = 0x160f00;
      *(undefined2 *)((long)puVar7 + 0xc) = 0xf05;
      puVar4 = _DAT_ffff00009088a210;
      _DAT_ffff00009088a210 = puVar7;
      *(undefined8 *)((long)puVar7 + 0xe) = 0x2021007020016;
      *(undefined8 *)((long)puVar7 + 0x16) = 0xe0003100a0000;
      *(undefined4 *)((long)puVar7 + 0x1e) = 0x200a01;
      *puVar7 = puVar4;
    }
    FUN_ffff00009078c134(lVar3,1);
    puVar7 = (undefined8 *)FUN_ffff00009077d514(0x14);
    if (puVar7 == (undefined8 *)0x0) {
      FUN_ffff00009077e11c(s__USB__alloc_desc_failed__type__d_ffff0000907e3ba7,3);
    }
    else {
      FUN_ffff00009077ee50(puVar7,0,0x14);
      puVar4 = puVar7;
      *puVar7 = _DAT_ffff00009088a210;
      _DAT_ffff00009088a210 = puVar4;
      puVar7[1] = 0x409030400040300;
    }
  }
  FUN_ffff00009078e368();
  _DAT_ffff000016703e98 = _DAT_ffff000016703e98 | 1;
  FUN_ffff00009077cea0(1);
  _DAT_ffff000016703e98 = _DAT_ffff000016703e98 & 0xfffffffe;
  _DAT_ffff000016703e00 = _DAT_ffff000016703e00 & 0xfffffffe;
  _DAT_ffff000016703e08 = _DAT_ffff000016703e08 & 0xfffffffe;
  _DAT_ffff000016703e50 = _DAT_ffff000016703e50 & 0xfffffff8;
  uVar8 = 0x501;
  if ((DAT_ffff00009088a21d >> 1 & 1) != 0) {
    _DAT_ffff000016703e30 = _DAT_ffff000016703e30 & 0xfffffff8;
    uVar8 = 0x501;
    if ((DAT_ffff00009088a21d & 2) != 0) {
      uVar8 = 0x10501;
    }
  }
  iVar6 = 100;
  pcVar9 = s__USB__SSUSB_SYS125_RST_B_STS_NG_ffff0000907d1fb1;
  do {
    if ((uVar8 & (_DAT_ffff000016703e10 ^ 0xffffffff)) == 0) {
      iVar6 = 100;
      pcVar9 = s__USB__SSUSB_U2_MAC_SYS_RST_B_STS_ffff0000907f8f18;
      goto LAB_ffff00009078bcd4;
    }
    FUN_ffff00009077cea0(100);
    iVar6 = iVar6 + -1;
  } while (iVar6 != 0);
  goto LAB_ffff00009078bcec;
  while( true ) {
    FUN_ffff00009077cea0(100);
    iVar6 = iVar6 + -1;
    if (iVar6 == 0) break;
LAB_ffff00009078bcd4:
    if ((_DAT_ffff000016703e14 & 1) != 0) goto LAB_ffff00009078bd00;
  }
LAB_ffff00009078bcec:
  FUN_ffff00009077e11c(pcVar9);
  FUN_ffff00009077e11c(s__USB__Refer_clocks_stability_che_ffff0000907ea1a2);
LAB_ffff00009078bd00:
  lVar2 = _DAT_ffff00009088a1b8;
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
  if ((DAT_ffff00009088a21d >> 1 & 1) != 0) {
    _DAT_ffff000016702610 = _DAT_ffff000016702610 & 0xfffffcfc;
    _DAT_ffff000016702410 = _DAT_ffff000016702410 & 0xfffffffb;
    _DAT_ffff000016703414 = _DAT_ffff000016703414 | 0x400;
  }
  _DAT_ffff000016703424 = _DAT_ffff000016703424 & 0xffff;
  _DAT_ffff000016701800 = 0;
  _DAT_ffff000016701400 = _DAT_ffff000016701400 | 1;
  _DAT_ffff000016701c84 = _DAT_ffff000016701c84 | 3;
  if (_DAT_ffff00009088a0cc == 2) {
    _DAT_ffff00001670241c = _DAT_ffff00001670241c & 0xfffffffe;
    _DAT_ffff000016703404 = _DAT_ffff000016703404 | 0x20;
  }
  else if (_DAT_ffff00009088a0cc == 1) {
    _DAT_ffff00001670241c = _DAT_ffff00001670241c & 0xfffffffe;
    _DAT_ffff000016703404 = _DAT_ffff000016703404 & 0xffffffdf;
  }
  else {
    _DAT_ffff000016703e30 = _DAT_ffff000016703e30 & 0xfffffdff;
  }
  _DAT_ffff000016701008 = 0x37;
  _DAT_ffff000016703418 = 7;
  if ((DAT_ffff00009088a21d >> 1 & 1) != 0) {
    _DAT_ffff00001670253c = 0x1a430;
  }
  _DAT_ffff000016701718 = 0x160006;
  _DAT_ffff000016701850 = 1;
  iVar6 = FUN_ffff00009077f654(_DAT_ffff00009088a1b8 + 0x30,&DAT_ffff0000907e3bca);
  if (iVar6 < 0) {
    FUN_ffff00009077e11c(s__USB__ep0_>name__copy_failed_ffff0000907ed407);
  }
  *(undefined1 *)(lVar2 + 0x42) = 0;
  *(undefined1 *)(lVar2 + 0x40) = 0;
  _DAT_ffff000016701100 = 0x200;
  if (_DAT_ffff00009088a0cc != 3) {
    _DAT_ffff000016701100 = 0x40;
  }
  *(undefined4 *)(lVar2 + 0x3c) = _DAT_ffff000016701100;
  _DAT_ffff000016701088 = _DAT_ffff000016701088 | 1;
  FUN_ffff000090727528(0x277,1);
  FUN_ffff00009072751c(0x277,0);
  FUN_ffff000090716128(0x277,FUN_ffff00009078c47c,0);
  FUN_ffff00009071654c(0x277);
  bVar5 = (2 < _DAT_ffff00009088a0cc & DAT_ffff00009088a21d >> 1) == 0;
  puVar1 = (uint *)&DAT_ffff00001670241c;
  if (bVar5) {
    puVar1 = (uint *)&DAT_ffff000016703404;
  }
  uVar8 = 0x41;
  if (!bVar5) {
    uVar8 = 1;
  }
  pcVar9 = s__USB__U3_pullup_D_s_ffff0000907fa9ac;
  if (bVar5) {
    pcVar9 = s__USB__U2_pullup_D_s_ffff0000907db9eb;
  }
  *puVar1 = uVar8 | *puVar1;
  FUN_ffff00009077e11c(pcVar9,&DAT_ffff0000907eef2c);
  DAT_ffff00009088a0b0 = 1;
  return 0;
}



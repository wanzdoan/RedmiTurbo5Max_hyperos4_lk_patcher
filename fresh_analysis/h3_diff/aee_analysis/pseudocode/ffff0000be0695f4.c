
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be0695f4(void)

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
  
  lVar3 = DAT_ffff0000be134748;
  lVar2 = DAT_ffff0000be134740;
  if (DAT_ffff0000be134740 == 0) {
    FUN_ffff0000be05fd98(s__USB__udc_cannot_start_before_in_ffff0000be0c36e0);
    return 0xffffffed;
  }
  if (DAT_ffff0000be134748 == 0) {
    FUN_ffff0000be05fd98(s__USB__udc_has_no_gadget_register_ffff0000be0b92c6);
    return 0xfffffffa;
  }
  FUN_ffff0000be069b80(DAT_ffff0000be134740,0);
  FUN_ffff0000be069cc0(lVar3,0);
  puVar7 = (undefined8 *)FUN_ffff0000be05f1bc(0x14);
  if (puVar7 == (undefined8 *)0x0) {
    FUN_ffff0000be05fd98(s__USB__alloc_desc_failed__type__d_ffff0000be0b157c,3);
  }
  else {
    FUN_ffff0000be060ae0(puVar7,0,0x14);
    puVar4 = puVar7;
    *puVar7 = DAT_ffff0000be134888;
    DAT_ffff0000be134888 = puVar4;
    puVar7[1] = 0x409030400040300;
  }
  if ((DAT_ffff0000be1348a5 >> 1 & 1) != 0) {
    FUN_ffff0000be069b80(lVar2,1);
    puVar7 = (undefined8 *)FUN_ffff0000be05f1bc(0x26);
    if (puVar7 == (undefined8 *)0x0) {
      FUN_ffff0000be05fd98(s__USB__alloc_desc_failed__type__d_ffff0000be0b157c,0xf);
    }
    else {
      FUN_ffff0000be060ae0(puVar7,0,0x26);
      *(undefined4 *)(puVar7 + 1) = 0x160f00;
      *(undefined2 *)((long)puVar7 + 0xc) = 0xf05;
      puVar4 = DAT_ffff0000be134898;
      DAT_ffff0000be134898 = puVar7;
      *(undefined8 *)((long)puVar7 + 0xe) = 0x2021007020016;
      *(undefined8 *)((long)puVar7 + 0x16) = 0xe0003100a0000;
      *(undefined4 *)((long)puVar7 + 0x1e) = 0x200a01;
      *puVar7 = puVar4;
    }
    FUN_ffff0000be069cc0(lVar3,1);
    puVar7 = (undefined8 *)FUN_ffff0000be05f1bc(0x14);
    if (puVar7 == (undefined8 *)0x0) {
      FUN_ffff0000be05fd98(s__USB__alloc_desc_failed__type__d_ffff0000be0b157c,3);
    }
    else {
      FUN_ffff0000be060ae0(puVar7,0,0x14);
      puVar4 = puVar7;
      *puVar7 = DAT_ffff0000be134898;
      DAT_ffff0000be134898 = puVar4;
      puVar7[1] = 0x409030400040300;
    }
  }
  FUN_ffff0000be06bef4();
  _DAT_ffff000016703e98 = _DAT_ffff000016703e98 | 1;
  FUN_ffff0000be05ede4(1);
  _DAT_ffff000016703e98 = _DAT_ffff000016703e98 & 0xfffffffe;
  _DAT_ffff000016703e00 = _DAT_ffff000016703e00 & 0xfffffffe;
  _DAT_ffff000016703e08 = _DAT_ffff000016703e08 & 0xfffffffe;
  _DAT_ffff000016703e50 = _DAT_ffff000016703e50 & 0xfffffff8;
  uVar8 = 0x501;
  if ((DAT_ffff0000be1348a5 >> 1 & 1) != 0) {
    _DAT_ffff000016703e30 = _DAT_ffff000016703e30 & 0xfffffff8;
    uVar8 = 0x501;
    if ((DAT_ffff0000be1348a5 & 2) != 0) {
      uVar8 = 0x10501;
    }
  }
  iVar6 = 100;
  pcVar9 = s__USB__SSUSB_SYS125_RST_B_STS_NG_ffff0000be0a508c;
  do {
    if ((uVar8 & (_DAT_ffff000016703e10 ^ 0xffffffff)) == 0) {
      iVar6 = 100;
      pcVar9 = s__USB__SSUSB_U2_MAC_SYS_RST_B_STS_ffff0000be0c0051;
      goto LAB_ffff0000be069860;
    }
    FUN_ffff0000be05ede4(100);
    iVar6 = iVar6 + -1;
  } while (iVar6 != 0);
  goto LAB_ffff0000be069878;
  while( true ) {
    FUN_ffff0000be05ede4(100);
    iVar6 = iVar6 + -1;
    if (iVar6 == 0) break;
LAB_ffff0000be069860:
    if ((_DAT_ffff000016703e14 & 1) != 0) goto LAB_ffff0000be06988c;
  }
LAB_ffff0000be069878:
  FUN_ffff0000be05fd98(pcVar9);
  FUN_ffff0000be05fd98(s__USB__Refer_clocks_stability_che_ffff0000be0b5c47);
LAB_ffff0000be06988c:
  lVar2 = DAT_ffff0000be134840;
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
  if ((DAT_ffff0000be1348a5 >> 1 & 1) != 0) {
    _DAT_ffff000016702610 = _DAT_ffff000016702610 & 0xfffffcfc;
    _DAT_ffff000016702410 = _DAT_ffff000016702410 & 0xfffffffb;
    _DAT_ffff000016703414 = _DAT_ffff000016703414 | 0x400;
  }
  _DAT_ffff000016703424 = _DAT_ffff000016703424 & 0xffff;
  _DAT_ffff000016701800 = 0;
  _DAT_ffff000016701400 = _DAT_ffff000016701400 | 1;
  _DAT_ffff000016701c84 = _DAT_ffff000016701c84 | 3;
  if (DAT_ffff0000be134754 == 2) {
    _DAT_ffff00001670241c = _DAT_ffff00001670241c & 0xfffffffe;
    _DAT_ffff000016703404 = _DAT_ffff000016703404 | 0x20;
  }
  else if (DAT_ffff0000be134754 == 1) {
    _DAT_ffff00001670241c = _DAT_ffff00001670241c & 0xfffffffe;
    _DAT_ffff000016703404 = _DAT_ffff000016703404 & 0xffffffdf;
  }
  else {
    _DAT_ffff000016703e30 = _DAT_ffff000016703e30 & 0xfffffdff;
  }
  _DAT_ffff000016701008 = 0x37;
  _DAT_ffff000016703418 = 7;
  if ((DAT_ffff0000be1348a5 >> 1 & 1) != 0) {
    _DAT_ffff00001670253c = 0x1a430;
  }
  _DAT_ffff000016701718 = 0x160006;
  _DAT_ffff000016701850 = 1;
  iVar6 = FUN_ffff0000be061134(DAT_ffff0000be134840 + 0x30,&DAT_ffff0000be0b159f);
  if (iVar6 < 0) {
    FUN_ffff0000be05fd98(s__USB__ep0_>name__copy_failed_ffff0000be0b8026);
  }
  *(undefined1 *)(lVar2 + 0x42) = 0;
  *(undefined1 *)(lVar2 + 0x40) = 0;
  _DAT_ffff000016701100 = 0x200;
  if (DAT_ffff0000be134754 != 3) {
    _DAT_ffff000016701100 = 0x40;
  }
  *(undefined4 *)(lVar2 + 0x3c) = _DAT_ffff000016701100;
  _DAT_ffff000016701088 = _DAT_ffff000016701088 | 1;
  FUN_ffff0000be021e1c(0x277,1);
  FUN_ffff0000be021e10(0x277,0);
  FUN_ffff0000be018fd4(0x277,&LAB_ffff0000be06a008,0);
  FUN_ffff0000be019544(0x277);
  bVar5 = (2 < DAT_ffff0000be134754 & DAT_ffff0000be1348a5 >> 1) == 0;
  puVar1 = (uint *)&DAT_ffff00001670241c;
  if (bVar5) {
    puVar1 = (uint *)&DAT_ffff000016703404;
  }
  uVar8 = 0x41;
  if (!bVar5) {
    uVar8 = 1;
  }
  pcVar9 = s__USB__U3_pullup_D_s_ffff0000be0c12a9;
  if (bVar5) {
    pcVar9 = s__USB__U2_pullup_D_s_ffff0000be0abe3d;
  }
  *puVar1 = uVar8 | *puVar1;
  FUN_ffff0000be05fd98(pcVar9,&DAT_ffff0000be0b92ea);
  DAT_ffff0000be134734 = 1;
  return 0;
}



// File offset entry: 0xa2890

void FUN_020a2790(undefined8 param_1,undefined8 param_2)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  int iVar4;
  long lVar5;
  ulong *puVar6;
  ulong uVar7;
  ulong uVar8;
  long *plVar9;
  ulong uVar10;
  ulong unaff_x21;
  ulong uVar11;
  undefined8 uVar12;
  uint unaff_w25;
  ulong unaff_x28;
  undefined8 uStack_950;
  undefined8 uStack_948;
  undefined8 uStack_940;
  ulong uStack_938;
  undefined8 uStack_930;
  undefined8 uStack_928;
  undefined8 uStack_920;
  undefined8 uStack_918;
  undefined8 uStack_910;
  undefined8 uStack_908;
  undefined8 uStack_900;
  undefined8 uStack_8f8;
  undefined8 uStack_8f0;
  undefined8 uStack_8e8;
  ulong auStack_8e0 [6];
  undefined8 uStack_8b0;
  undefined8 uStack_8a8;
  undefined8 uStack_8a0;
  undefined8 uStack_898;
  undefined8 uStack_890;
  undefined8 uStack_888;
  undefined8 uStack_880;
  undefined8 uStack_878;
  undefined8 uStack_870;
  undefined8 uStack_868;
  undefined8 uStack_860;
  undefined8 uStack_858;
  undefined8 uStack_850;
  undefined1 uStack_848;
  undefined8 uStack_840;
  undefined8 uStack_838;
  undefined8 uStack_830;
  undefined8 uStack_828;
  undefined8 uStack_820;
  undefined1 uStack_818;
  undefined1 auStack_814 [96];
  undefined1 auStack_7b4 [96];
  undefined1 auStack_754 [96];
  undefined1 auStack_6f4 [128];
  undefined1 auStack_674 [1492];
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 uStack_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined8 uStack_78;
  long lStack_70;
  
  lStack_70 = *plRam0000000002103280;
  uStack_80 = 0;
  uStack_78 = 0;
  uStack_90 = 0;
  uStack_88 = 0;
  uStack_a0 = 0;
  uStack_98 = 0;
  uStack_930 = 0;
  uStack_928 = 0;
  uStack_938 = 0;
  FUN_02091664(auStack_674,0,0x5d4);
  FUN_02091664(auStack_6f4,0,0x80);
  FUN_02091664(auStack_754,0,0x60);
  FUN_02091664(auStack_7b4,0,0x60);
  FUN_02091664(auStack_814,0,0x60);
  iVar1 = iRam00000000021224e0;
  uStack_818 = 0;
  uStack_828 = 0;
  uStack_820 = 0;
  uStack_838 = 0;
  uStack_830 = 0;
  uStack_840 = 0;
  uStack_848 = 0;
  uStack_858 = 0;
  uStack_850 = 0;
  uStack_868 = 0;
  uStack_860 = 0;
  uStack_870 = 0;
  if (iRam00000000021224e0 == 0) {
    uVar12 = 0x40;
    uVar11 = 0x20;
  }
  else {
    if (iRam00000000021224e0 != 1) {
      uVar3 = 0x10a007;
      goto code_r0x020a2ab8;
    }
    uVar12 = 0x60;
    uVar11 = 0x30;
  }
  uVar3 = func_0x020a2e08(param_1,uVar11,&uStack_a0,auStack_754,2);
  unaff_x21 = uVar11;
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  uVar3 = func_0x020a2e08(param_2,uVar11,&uStack_a0,auStack_7b4,1);
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  uVar3 = func_0x020a3150(auStack_7b4,uVar12,&uStack_840);
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  uVar3 = func_0x020a3150(auStack_754,uVar12,&uStack_870);
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  uVar3 = func_0x020a3290(auStack_754,uVar12,auStack_6f4,&uStack_928);
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  uVar3 = func_0x020a3400(&uStack_840,&uStack_870,auStack_6f4,uStack_928,auStack_674,&uStack_930);
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  uStack_880 = 0;
  uStack_878 = 0;
  uStack_940 = 0;
  uVar3 = func_0x020a35d0(uVar11,&uStack_880,&uStack_940);
  uVar2 = uStack_930;
  uVar12 = uStack_940;
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  uVar3 = func_0x020a3690(&uStack_880,uStack_940,auStack_674,uStack_930,
                          (ulong)uRam0000000002122b94 + 0x21224e8,
                          iRam0000000002122b9c - uRam0000000002122b94,&uStack_938);
  if ((int)uVar3 != 0) goto code_r0x020a2ab8;
  unaff_x21 = uVar11 >> 3;
  uStack_950 = uVar2;
  uStack_948 = uVar12;
  unaff_w25 = 0;
  uStack_890 = 0;
  uStack_888 = 0;
  uStack_8a0 = 0;
  uStack_898 = 0;
  uStack_8b0 = 0;
  uStack_8a8 = 0;
  auStack_8e0[4] = 0;
  auStack_8e0[5] = 0;
  auStack_8e0[2] = 0;
  auStack_8e0[3] = 0;
  auStack_8e0[0] = 0;
  auStack_8e0[1] = 0;
LAB_020a29ac:
  uVar10 = 0;
  do {
    uVar3 = FUN_0208bac0((long)&uStack_8b0 + uVar10);
    unaff_x28 = unaff_x21;
    if ((int)uVar3 != 0) goto code_r0x020a2ab8;
    uVar10 = uVar10 + 4;
  } while (uVar11 != uVar10);
  func_0x020a3784(auStack_8e0,&uStack_8b0,unaff_x21);
  lVar5 = 0;
  do {
    if (*(long *)((long)auStack_8e0 + lVar5) != 0) {
      uVar10 = unaff_x21;
      if (iVar1 == 0) goto LAB_020a2a3c;
      goto LAB_020a2a10;
    }
    lVar5 = lVar5 + 8;
  } while (unaff_x21 << 3 != lVar5);
  goto LAB_020a29f8;
  while( true ) {
    uVar7 = *(ulong *)(uVar10 * 8 + 0x20e3c48);
    if (auStack_8e0[uVar10] < uVar7) goto LAB_020a2b40;
    if (uVar7 < auStack_8e0[uVar10]) break;
LAB_020a2a10:
    iVar4 = (int)uVar10;
    uVar10 = (ulong)(iVar4 - 1);
    if (iVar4 < 1) goto LAB_020a29f8;
  }
  if (9 < unaff_w25) goto LAB_020a2af4;
  goto LAB_020a29f8;
  while( true ) {
    uVar8 = *(ulong *)(uVar7 * 8 + 0x20dd980);
    if (auStack_8e0[uVar7] < uVar8) goto LAB_020a2b40;
    uVar10 = uVar7;
    if (uVar8 < auStack_8e0[uVar7]) break;
LAB_020a2a3c:
    uVar7 = (ulong)((int)uVar10 - 1);
    if ((int)uVar10 < 1) goto LAB_020a29f8;
  }
  if (9 < unaff_w25) {
    uVar11 = 0;
    puVar6 = auStack_8e0;
    uVar10 = unaff_x21;
    plVar9 = (long *)0x20dd980;
    do {
      uVar7 = *plVar9 + uVar11;
      if (uVar7 != 0) {
        uVar11 = (ulong)(*puVar6 < uVar7);
      }
      uVar10 = uVar10 - 1;
      *puVar6 = *puVar6 - uVar7;
      puVar6 = puVar6 + 1;
      plVar9 = plVar9 + 1;
      if (uVar10 == 0) goto LAB_020a2b30;
    } while( true );
  }
LAB_020a29f8:
  unaff_w25 = unaff_w25 + 1;
  if (unaff_w25 == 0x32) goto LAB_020a2b40;
  goto LAB_020a29ac;
LAB_020a2b40:
  puVar6 = auStack_8e0;
  do {
    uRam0000000002122b88 = unaff_w25;
    if (*puVar6 != 0) {
      if (iVar1 != 0) goto LAB_020a2b7c;
      goto LAB_020a2bb8;
    }
    unaff_x28 = unaff_x28 - 1;
    puVar6 = puVar6 + 1;
  } while (unaff_x28 != 0);
  goto LAB_020a2b60;
  while( true ) {
    uVar11 = *(ulong *)(unaff_x21 * 8 + 0x20dd980);
    if (auStack_8e0[unaff_x21] < uVar11) goto LAB_020a2bc4;
    if (uVar11 < auStack_8e0[unaff_x21]) break;
LAB_020a2bb8:
    iVar4 = (int)unaff_x21;
    unaff_x21 = (ulong)(iVar4 - 1);
    if (iVar4 < 1) break;
  }
  goto LAB_020a2b60;
  while( true ) {
    uVar11 = *(ulong *)(unaff_x21 * 8 + 0x20e3c48);
    if (auStack_8e0[unaff_x21] < uVar11) goto LAB_020a2bc4;
    if (uVar11 < auStack_8e0[unaff_x21]) break;
LAB_020a2b7c:
    iVar4 = (int)unaff_x21;
    unaff_x21 = (ulong)(iVar4 - 1);
    if (iVar4 < 1) break;
  }
LAB_020a2b60:
  uVar3 = 0x10a005;
code_r0x020a2ab8:
  if (lRam0000000002100880 == lStack_70) {
    return;
  }
  FUN_02091090(uVar3);
LAB_020a2af4:
  uVar11 = 0;
  puVar6 = auStack_8e0;
  uVar10 = unaff_x28;
  plVar9 = (long *)0x20e3c48;
  do {
    uVar7 = *plVar9 + uVar11;
    if (uVar7 != 0) {
      uVar11 = (ulong)(*puVar6 < uVar7);
    }
    uVar10 = uVar10 - 1;
    *puVar6 = *puVar6 - uVar7;
    puVar6 = puVar6 + 1;
    plVar9 = plVar9 + 1;
  } while (uVar10 != 0);
LAB_020a2b30:
  func_0x020a37f4(&uStack_8b0,auStack_8e0,unaff_x21 & 0xffffffff);
  goto LAB_020a2b40;
LAB_020a2bc4:
  uStack_8f0 = 0;
  uStack_8e8 = 0;
  uStack_900 = 0;
  uStack_8f8 = 0;
  uStack_910 = 0;
  uStack_908 = 0;
  uStack_920 = 0;
  uStack_918 = 0;
  uVar3 = FUN_0209c58c((ulong)uRam0000000002122b94 + 0x21224e8,uStack_938 & 0xffffffff,&uStack_920,
                       iRam00000000021224e0);
  if (((int)uVar3 == 0) &&
     (uVar3 = func_0x0209c674(&uStack_a0,&uStack_8b0,&uStack_920,auStack_814,
                              auStack_814 + (uRam0000000002122b8c >> 1),iRam00000000021224e0),
     (int)uVar3 == 0)) {
    uVar3 = func_0x020a3870(&uStack_880,uStack_948,auStack_674,uStack_950,auStack_814,
                            uRam0000000002122b8c,(ulong)uRam0000000002122b94 + 0x21224e8,
                            iRam0000000002122b9c - uRam0000000002122b94,&uStack_938);
  }
  goto code_r0x020a2ab8;
}



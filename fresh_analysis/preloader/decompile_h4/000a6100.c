// File offset entry: 0xa6100

undefined8 FUN_020a6000(void)

{
  uint uVar1;
  byte bVar2;
  undefined4 *puVar3;
  int *piVar4;
  bool bVar5;
  int iVar6;
  int iVar7;
  int iVar8;
  undefined4 uVar9;
  undefined8 uVar10;
  undefined8 uVar11;
  uint uVar12;
  uint uVar13;
  byte *pbVar14;
  long lVar15;
  int iStack_a4;
  uint uStack_a0;
  int iStack_9c;
  long alStack_98 [9];
  
  alStack_98[8] = *plRam0000000002103280;
  iStack_a4 = 0x200;
  iStack_9c = 0;
  alStack_98[0] = 0;
  uRam0000000002123ae0 = uRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  if (iRam0000000002123850 != 0) {
LAB_020a6064:
    uStack_a0 = 0;
    iVar6 = func_0x020a6c9c(0x30,alStack_98,&iStack_9c,&uStack_a0,0);
    if (iVar6 != 0) goto LAB_020a60a4;
    uStack_a0 = 0;
    iVar6 = func_0x020a6c9c(0x30,alStack_98,&iStack_9c,&uStack_a0,2);
    lVar15 = alStack_98[0];
    if (iVar6 != 0) goto LAB_020a60a4;
    bVar2 = *(byte *)(alStack_98[0] + 1);
    uVar12 = (uint)bVar2;
    if (((char)bVar2 < '\0') && (uVar1 = uVar12 & 0x7f, (bVar2 & 0x7f) != 0)) {
      uVar13 = uVar12 & 0x7f;
      uVar12 = 0;
      pbVar14 = (byte *)(alStack_98[0] + 2);
      do {
        uVar13 = uVar13 - 1;
        uVar12 = (uint)*pbVar14 | uVar12 << 8;
        pbVar14 = pbVar14 + 1;
      } while (uVar13 != 0);
      iVar6 = uVar1 + 2;
    }
    else {
      iVar6 = 2;
    }
    iStack_9c = 0;
    alStack_98[0] = 0;
    uRam0000000002123ae0 = uRam0000000002123840;
    iRam0000000002123ae8 = iRam0000000002123850;
    if (iRam0000000002123850 == 0) goto LAB_020a60ac;
    while( true ) {
      iVar7 = func_0x020a6a40(0);
      if (iVar7 != 0) goto LAB_020a60ac;
      uStack_a0 = 0;
      iVar7 = func_0x020a6c9c(0x30,alStack_98,&iStack_9c,&uStack_a0,2);
      if (iVar7 == 0) break;
LAB_020a61c8:
      if (iRam0000000002123ae8 == 0) goto LAB_020a60ac;
    }
    uStack_a0 = 0;
    iVar7 = func_0x020a6c9c(3,alStack_98,&iStack_9c,&uStack_a0,0);
    if ((iVar7 != 0) ||
       (iVar8 = func_0x020a6de8(alStack_98[0],0x2123eec,0x200,&iStack_a4), iVar7 = iStack_a4,
       iVar8 != 0)) goto LAB_020a61c8;
    FUN_020a0fec(0x2123eec,iStack_a4);
    puVar3 = puRam0000000002103810;
    uRam0000000002123ae0 = 0;
    iRam0000000002123ae8 = 0;
    alStack_98[6] = 0;
    alStack_98[7] = 0;
    alStack_98[4] = 0;
    alStack_98[5] = 0;
    alStack_98[2] = 0;
    alStack_98[3] = 0;
    alStack_98[0] = 0;
    alStack_98[1] = 0;
    iStack_9c = 0;
    iVar8 = FUN_0209c58c(lVar15,uVar12 + iVar6,alStack_98,*puRam0000000002103810);
    piVar4 = piRam0000000002103818;
    if (iVar8 == 0) {
      if (*piRam0000000002103818 == iVar7) {
        func_0x020a0dfc(0x2123aec,0x2123eec,iVar7);
        uVar9 = func_0x020a7288(0);
        func_0x020a0fac(0x2123aec,*piVar4);
        func_0x0209c5c0(0x2123aec,0x2123858,0x2123cec,*puVar3);
        func_0x020a0fac(0x2123cec,*piVar4);
        func_0x020a7288(uVar9);
        iVar7 = func_0x0209c600(alStack_98,0x2123cec,&iStack_9c,*puVar3);
        if (iVar7 == 0) {
          if (iStack_9c == 1) {
            if (*piRam0000000002103840 != 0) {
              func_0x0208bac0(&uStack_a0);
              func_0x02091d5c(uStack_a0 % 0x14);
              iVar7 = func_0x0209c600(alStack_98,0x2123cec,&iStack_9c,*puVar3);
              if (iVar7 != 0) goto LAB_020a62c8;
              if (iStack_9c != 1) goto LAB_020a635c;
            }
            uVar10 = 0;
            lRam0000000002123848 = lVar15;
            iRam0000000002123854 = uVar12 + iVar6;
          }
          else {
LAB_020a635c:
            uVar10 = 0x102006;
          }
        }
        else {
LAB_020a62c8:
          uVar10 = 0x102005;
        }
      }
      else {
        uVar10 = 0x101007;
      }
    }
    else {
      uVar10 = 0x102004;
    }
    goto LAB_020a60bc;
  }
LAB_020a60ac:
  uRam0000000002123ae0 = 0;
  uVar10 = 0x101012;
  iRam0000000002123ae8 = 0;
LAB_020a60bc:
  if (*plRam0000000002103280 == alStack_98[8]) {
    return uVar10;
  }
  uVar10 = FUN_02091090(uVar10);
  uRam0000000002123ae0 = uRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  if (iRam0000000002123850 != 0) {
    uVar11 = func_0x020a6a40(3);
    if ((int)uVar11 == 0) {
      do {
        uVar11 = func_0x020a6fa8(uVar10);
        bVar5 = (int)uVar11 == 0;
        if (((int)uVar11 == 0) || (iRam0000000002123ae8 == 0)) break;
        uVar11 = func_0x020a6a40(3);
      } while ((int)uVar11 == 0);
    }
    else {
      bVar5 = false;
    }
    if (bVar5) {
      uRam0000000002123ae0 = 0;
      iRam0000000002123ae8 = 0;
      return uVar11;
    }
  }
  uRam0000000002123ae0 = 0;
  iRam0000000002123ae8 = 0;
  return 0x101012;
LAB_020a60a4:
  if (iRam0000000002123ae8 == 0) goto LAB_020a60ac;
  goto LAB_020a6064;
}



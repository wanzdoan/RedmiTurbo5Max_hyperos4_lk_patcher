
undefined8 FUN_ffff0000be040fcc(int *param_1,long param_2,int param_3)

{
  int iVar1;
  int *piVar2;
  int *piVar3;
  uint uVar4;
  int iVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  bool bVar9;
  int iVar10;
  int iVar11;
  int iVar12;
  int iVar13;
  int iVar14;
  uint uVar15;
  undefined4 uVar16;
  long lVar17;
  undefined4 uVar18;
  undefined4 uVar19;
  undefined4 uVar20;
  uint uVar21;
  int iVar22;
  int iVar23;
  uint uVar24;
  int iVar25;
  
  FUN_ffff0000be0607b0(param_2,param_1,0x998);
  if (param_1[1] != 0) {
    if ((char)param_1[0x264] == '\0') {
      uVar21 = param_1[0x24a];
      uVar24 = 0;
    }
    else {
      uVar21 = (uint)param_1[0xa5] >> 1;
      uVar24 = (uint)param_1[0xa6] >> 1;
    }
    iVar13 = *(int *)(param_2 + 0x294);
    uVar16 = *(undefined4 *)(param_2 + 0x288);
    uVar20 = *(undefined4 *)(param_2 + 0x28c);
    uVar19 = *(undefined4 *)(param_2 + 0x290);
    uVar18 = *(undefined4 *)(param_2 + 0x298);
    if (param_3 == 0) {
      iVar22 = (-(DAT_ffff0000be125740 & 1) & DAT_ffff0000be125738._4_4_) + uVar24;
      if (iVar22 == 0) {
        uVar18 = 0;
        uVar19 = 0;
        uVar16 = 0;
        uVar20 = 0;
        iVar23 = 0;
      }
      else {
        iVar23 = 0;
        if ((uint)-iVar22 <= iVar13 - 1U) {
          iVar23 = iVar22 + iVar13;
        }
      }
      *(undefined4 *)(param_2 + 0x28c) = uVar20;
      *(undefined4 *)(param_2 + 0x288) = uVar16;
      *(undefined4 *)(param_2 + 0x290) = uVar19;
      *(int *)(param_2 + 0x294) = iVar23;
      *(undefined4 *)(param_2 + 0x298) = uVar18;
      *(undefined4 *)(param_2 + 0x10) = 0;
      iVar13 = *param_1;
      goto joined_r0xffff0000be04123c;
    }
    iVar22 = (-(DAT_ffff0000be125740 & 1) & DAT_ffff0000be125730._4_4_) + uVar21;
    if (iVar22 == 0) {
      uVar18 = 0;
      uVar19 = 0;
      uVar16 = 0;
      uVar20 = 0;
    }
    else if (iVar13 - 1U <= iVar22 - 1U) {
      iVar22 = iVar13;
    }
    *(undefined4 *)(param_2 + 0x28c) = uVar20;
    *(undefined4 *)(param_2 + 0x288) = uVar16;
    *(undefined4 *)(param_2 + 0x290) = uVar19;
    *(int *)(param_2 + 0x294) = iVar22;
    *(undefined4 *)(param_2 + 0x298) = uVar18;
  }
  iVar13 = *param_1;
joined_r0xffff0000be04123c:
  if (iVar13 != 0) {
    lVar17 = 0;
    uVar6 = param_1[0x24a];
    uVar21 = -(DAT_ffff0000be125740 & 1) & DAT_ffff0000be125730._4_4_;
    uVar24 = -(DAT_ffff0000be125740 & 1) & DAT_ffff0000be125738._4_4_;
    do {
      piVar2 = (int *)(param_2 + 0x28 + lVar17);
      if (piVar2[-3] != 0) {
        iVar22 = piVar2[6];
        uVar4 = piVar2[7];
        iVar23 = piVar2[-1];
        iVar25 = *piVar2;
        bVar9 = (char)param_1[0x264] != '\0';
        iVar13 = piVar2[2];
        iVar10 = piVar2[3];
        iVar5 = piVar2[4];
        uVar8 = uVar6;
        if (bVar9) {
          uVar8 = uVar6 >> 1;
        }
        uVar15 = 0;
        if (bVar9) {
          uVar15 = uVar6 >> 1;
        }
        iVar11 = piVar2[5];
        iVar12 = piVar2[8];
        uVar7 = (uVar4 + iVar5) - 1;
        if (param_3 == 0) {
          iVar14 = uVar24 + uVar15;
          if (iVar14 == 0) goto LAB_ffff0000be041188;
          uVar8 = uVar6 - iVar14;
          if (uVar7 < uVar8) goto LAB_ffff0000be041178;
          uVar15 = uVar4 - uVar8;
          iVar14 = iVar5;
          if (uVar4 < uVar8 || uVar4 - uVar8 == 0) {
            iVar14 = (uVar4 + iVar5) - uVar8;
            if (iVar25 == 0) {
              iVar13 = 0;
              uVar15 = 0;
            }
            else {
              iVar13 = (iVar13 - uVar4) + uVar8;
              uVar15 = 0;
            }
          }
        }
        else {
          iVar1 = uVar21 + uVar8;
          if (iVar1 == 0) {
LAB_ffff0000be041188:
            iVar13 = 0;
            iVar10 = 0;
            iVar25 = 0;
            iVar23 = 0;
            iVar22 = 0;
            iVar12 = 0;
            iVar11 = 0;
            uVar15 = 0;
            iVar14 = 0;
          }
          else if (iVar1 - 1U < uVar4) {
LAB_ffff0000be041178:
            iVar13 = 0;
            uVar15 = 0;
            iVar14 = 0;
          }
          else {
            uVar15 = uVar4;
            iVar14 = iVar1 - uVar4;
            if (uVar7 <= iVar1 - 1U) {
              iVar14 = iVar5;
            }
          }
        }
        piVar3 = (int *)(param_2 + 0x28 + lVar17);
        piVar3[2] = iVar13;
        piVar3[3] = iVar10;
        piVar3[-1] = iVar23;
        *piVar3 = iVar25;
        piVar3[6] = iVar22;
        piVar3[7] = uVar15;
        piVar3[8] = iVar12;
        piVar3[9] = iVar14;
        piVar3[10] = iVar11;
        if (iVar14 == 0) {
          piVar2[-3] = 0;
        }
      }
      lVar17 = lVar17 + 0x68;
    } while (lVar17 != 0x270);
    if (param_3 == 0) {
      *(undefined4 *)(param_2 + 0x10) = 0;
    }
  }
  return 0;
}




void FUN_ffff0000b804c464(undefined8 *param_1,ulong param_2,ulong param_3,code *param_4)

{
  undefined8 *puVar1;
  undefined1 uVar2;
  bool bVar3;
  bool bVar4;
  int iVar5;
  undefined8 *puVar6;
  char cVar7;
  int iVar8;
  long lVar9;
  ulong uVar10;
  ulong uVar11;
  long lVar12;
  ulong uVar13;
  undefined8 *puVar14;
  ulong uVar15;
  ulong uVar16;
  undefined8 uVar17;
  uint uVar18;
  long lVar19;
  ulong uVar20;
  undefined8 *puVar21;
  undefined8 *puVar22;
  undefined8 *puVar23;
  long lVar24;
  
  uVar18 = (uint)param_3;
  bVar4 = (((uint)param_1 | uVar18) & 7) == 0;
  cVar7 = param_3 != 8;
  if (!bVar4) {
    cVar7 = '\x02';
  }
  if (6 < param_2) {
    uVar13 = (ulong)(int)uVar18;
    lVar24 = -param_3;
    lVar12 = uVar13 + 1;
    uVar16 = (uVar13 >> 3) + 1;
    do {
      puVar22 = (undefined8 *)((long)param_1 + (param_2 >> 1) * param_3);
      if (param_2 != 7) {
        puVar23 = (undefined8 *)((long)param_1 + (param_2 - 1) * param_3);
        puVar21 = puVar23;
        puVar14 = param_1;
        if (0x28 < param_2) {
          iVar8 = (int)(param_2 >> 3) * uVar18;
          lVar19 = (long)iVar8;
          puVar14 = (undefined8 *)((long)param_1 + lVar19);
          lVar9 = (long)(iVar8 * 2);
          puVar21 = (undefined8 *)((long)param_1 + lVar9);
          iVar8 = (*param_4)(param_1,puVar14);
          iVar5 = (*param_4)(puVar14,puVar21);
          if (iVar8 < 0) {
            if ((-1 < iVar5) && (iVar8 = (*param_4)(param_1,puVar21), puVar14 = puVar21, -1 < iVar8)
               ) {
              puVar14 = param_1;
            }
          }
          else if ((iVar5 < 1) &&
                  (iVar8 = (*param_4)(param_1,puVar21), puVar14 = param_1, -1 < iVar8)) {
            puVar14 = puVar21;
          }
          puVar6 = (undefined8 *)((long)puVar22 - lVar19);
          puVar21 = (undefined8 *)((long)puVar22 + lVar19);
          iVar8 = (*param_4)(puVar6,puVar22);
          iVar5 = (*param_4)(puVar22,puVar21);
          if (iVar8 < 0) {
            if ((-1 < iVar5) && (iVar8 = (*param_4)(puVar6,puVar21), puVar22 = puVar21, -1 < iVar8))
            {
              puVar22 = puVar6;
            }
          }
          else if ((iVar5 < 1) && (iVar8 = (*param_4)(puVar6,puVar21), puVar22 = puVar6, -1 < iVar8)
                  ) {
            puVar22 = puVar21;
          }
          puVar21 = (undefined8 *)((long)puVar23 - lVar19);
          puVar6 = (undefined8 *)((long)puVar23 - lVar9);
          iVar8 = (*param_4)(puVar6,puVar21);
          iVar5 = (*param_4)(puVar21,puVar23);
          if (iVar8 < 0) {
            if ((-1 < iVar5) && (iVar8 = (*param_4)(puVar6,puVar23), puVar21 = puVar23, -1 < iVar8))
            {
              puVar21 = puVar6;
            }
          }
          else if ((iVar5 < 1) && (iVar8 = (*param_4)(puVar6,puVar23), puVar21 = puVar6, -1 < iVar8)
                  ) {
            puVar21 = puVar23;
          }
        }
        iVar8 = (*param_4)(puVar14,puVar22);
        iVar5 = (*param_4)(puVar22,puVar21);
        if (iVar8 < 0) {
          if ((-1 < iVar5) && (iVar8 = (*param_4)(puVar14,puVar21), puVar22 = puVar21, -1 < iVar8))
          {
            puVar22 = puVar14;
          }
        }
        else if ((iVar5 < 1) && (iVar8 = (*param_4)(puVar14,puVar21), puVar22 = puVar14, -1 < iVar8)
                ) {
          puVar22 = puVar21;
        }
      }
      if (cVar7 == '\0') {
        uVar17 = *param_1;
        *param_1 = *puVar22;
        *puVar22 = uVar17;
      }
      else {
        lVar9 = lVar12;
        uVar10 = uVar16;
        puVar14 = param_1;
        if (bVar4) {
          do {
            uVar10 = uVar10 - 1;
            uVar17 = *puVar14;
            *puVar14 = *puVar22;
            *puVar22 = uVar17;
            puVar14 = puVar14 + 1;
            puVar22 = puVar22 + 1;
          } while (1 < uVar10);
        }
        else {
          do {
            lVar9 = lVar9 + -1;
            uVar2 = *(undefined1 *)puVar14;
            *(undefined1 *)puVar14 = *(undefined1 *)puVar22;
            *(undefined1 *)puVar22 = uVar2;
            puVar14 = (undefined8 *)((long)puVar14 + 1);
            puVar22 = (undefined8 *)((long)puVar22 + 1);
          } while (1 < lVar9);
        }
      }
      puVar22 = (undefined8 *)((long)param_1 + param_3);
      bVar3 = false;
      puVar21 = (undefined8 *)((long)param_1 + (param_2 - 1) * param_3);
      puVar23 = puVar22;
      puVar6 = puVar21;
      puVar14 = puVar22;
      if (puVar22 <= puVar21) goto LAB_ffff0000b804c7b0;
LAB_ffff0000b804c844:
      if (puVar23 <= puVar6) {
        iVar8 = (*param_4)(puVar6,param_1);
        if (iVar8 < 0) {
          if (cVar7 == '\0') {
            uVar17 = *puVar23;
            *puVar23 = *puVar6;
            *puVar6 = uVar17;
          }
          else {
            lVar9 = 0;
            lVar19 = lVar12;
            uVar10 = uVar16;
            if (bVar4) {
              do {
                uVar10 = uVar10 - 1;
                uVar17 = *(undefined8 *)((long)puVar23 + lVar9);
                *(undefined8 *)((long)puVar23 + lVar9) = *(undefined8 *)((long)puVar6 + lVar9);
                *(undefined8 *)((long)puVar6 + lVar9) = uVar17;
                lVar9 = lVar9 + 8;
              } while (1 < uVar10);
            }
            else {
              do {
                lVar19 = lVar19 + -1;
                uVar2 = *(undefined1 *)((long)puVar23 + lVar9);
                *(undefined1 *)((long)puVar23 + lVar9) = *(undefined1 *)((long)puVar6 + lVar9);
                *(undefined1 *)((long)puVar6 + lVar9) = uVar2;
                lVar9 = lVar9 + 1;
              } while (1 < lVar19);
            }
          }
          puVar23 = (undefined8 *)((long)puVar23 + param_3);
          puVar6 = (undefined8 *)((long)puVar6 + lVar24);
          bVar3 = true;
          if (puVar23 <= puVar6) {
LAB_ffff0000b804c7b0:
            do {
              iVar8 = (*param_4)(puVar23,param_1);
              if (0 < iVar8) break;
              if (iVar8 == 0) {
                if (cVar7 == '\0') {
                  uVar17 = *puVar14;
                  *puVar14 = *puVar23;
                  *puVar23 = uVar17;
                }
                else {
                  lVar9 = 0;
                  lVar19 = lVar12;
                  uVar10 = uVar16;
                  if (bVar4) {
                    do {
                      uVar10 = uVar10 - 1;
                      uVar17 = *(undefined8 *)((long)puVar14 + lVar9);
                      *(undefined8 *)((long)puVar14 + lVar9) =
                           *(undefined8 *)((long)puVar23 + lVar9);
                      *(undefined8 *)((long)puVar23 + lVar9) = uVar17;
                      lVar9 = lVar9 + 8;
                    } while (1 < uVar10);
                  }
                  else {
                    do {
                      lVar19 = lVar19 + -1;
                      uVar2 = *(undefined1 *)((long)puVar14 + lVar9);
                      *(undefined1 *)((long)puVar14 + lVar9) =
                           *(undefined1 *)((long)puVar23 + lVar9);
                      *(undefined1 *)((long)puVar23 + lVar9) = uVar2;
                      lVar9 = lVar9 + 1;
                    } while (1 < lVar19);
                  }
                }
                puVar14 = (undefined8 *)((long)puVar14 + param_3);
                bVar3 = true;
              }
              puVar23 = (undefined8 *)((long)puVar23 + param_3);
            } while (puVar23 <= puVar6);
          }
        }
        else {
          if (iVar8 == 0) {
            if (cVar7 == '\0') {
              uVar17 = *puVar6;
              *puVar6 = *puVar21;
              *puVar21 = uVar17;
            }
            else {
              lVar9 = 0;
              lVar19 = lVar12;
              uVar10 = uVar16;
              if (bVar4) {
                do {
                  uVar10 = uVar10 - 1;
                  uVar17 = *(undefined8 *)((long)puVar6 + lVar9);
                  *(undefined8 *)((long)puVar6 + lVar9) = *(undefined8 *)((long)puVar21 + lVar9);
                  *(undefined8 *)((long)puVar21 + lVar9) = uVar17;
                  lVar9 = lVar9 + 8;
                } while (1 < uVar10);
              }
              else {
                do {
                  lVar19 = lVar19 + -1;
                  uVar2 = *(undefined1 *)((long)puVar6 + lVar9);
                  *(undefined1 *)((long)puVar6 + lVar9) = *(undefined1 *)((long)puVar21 + lVar9);
                  *(undefined1 *)((long)puVar21 + lVar9) = uVar2;
                  lVar9 = lVar9 + 1;
                } while (1 < lVar19);
              }
            }
            puVar21 = (undefined8 *)((long)puVar21 + lVar24);
            bVar3 = true;
          }
          puVar6 = (undefined8 *)((long)puVar6 + lVar24);
        }
        goto LAB_ffff0000b804c844;
      }
      lVar9 = param_2 * param_3;
      puVar1 = (undefined8 *)((long)param_1 + lVar9);
      if (!bVar3) {
        if (lVar9 - param_3 == 0 || lVar9 < (long)param_3) {
          return;
        }
        puVar14 = param_1;
        puVar21 = param_1;
        puVar23 = puVar22;
        goto joined_r0xffff0000b804cc04;
      }
      uVar11 = (long)puVar23 - (long)puVar14;
      uVar10 = (long)puVar14 - (long)param_1;
      if ((long)uVar11 <= (long)puVar14 - (long)param_1) {
        uVar10 = uVar11;
      }
      if (0 < (int)uVar10) {
        uVar10 = uVar10 & 0xffffffff;
        if (bVar4) {
          uVar15 = (uVar10 >> 3) + 1;
          puVar22 = (undefined8 *)((long)puVar23 - uVar10);
          puVar14 = param_1;
          do {
            uVar15 = uVar15 - 1;
            uVar17 = *puVar14;
            *puVar14 = *puVar22;
            *puVar22 = uVar17;
            puVar22 = puVar22 + 1;
            puVar14 = puVar14 + 1;
          } while (1 < uVar15);
        }
        else {
          uVar15 = uVar10 + 1;
          puVar22 = (undefined8 *)((long)puVar23 - uVar10);
          puVar14 = param_1;
          do {
            uVar15 = uVar15 - 1;
            uVar2 = *(undefined1 *)puVar14;
            *(undefined1 *)puVar14 = *(undefined1 *)puVar22;
            *(undefined1 *)puVar22 = uVar2;
            puVar22 = (undefined8 *)((long)puVar22 + 1);
            puVar14 = (undefined8 *)((long)puVar14 + 1);
          } while (1 < uVar15);
        }
      }
      uVar20 = (long)puVar21 - (long)puVar6;
      uVar15 = (long)puVar1 - (long)(uVar13 + (long)puVar21);
      uVar10 = uVar20;
      if ((long)uVar15 <= (long)uVar20) {
        uVar10 = uVar15;
      }
      if (0 < (int)uVar10) {
        uVar10 = uVar10 & 0xffffffff;
        if (bVar4) {
          uVar15 = (uVar10 >> 3) + 1;
          puVar22 = (undefined8 *)((long)puVar1 - uVar10);
          do {
            uVar15 = uVar15 - 1;
            uVar17 = *puVar23;
            *puVar23 = *puVar22;
            *puVar22 = uVar17;
            puVar22 = puVar22 + 1;
            puVar23 = puVar23 + 1;
          } while (1 < uVar15);
        }
        else {
          uVar15 = uVar10 + 1;
          puVar22 = (undefined8 *)((long)puVar1 - uVar10);
          do {
            uVar15 = uVar15 - 1;
            uVar2 = *(undefined1 *)puVar23;
            *(undefined1 *)puVar23 = *(undefined1 *)puVar22;
            *(undefined1 *)puVar22 = uVar2;
            puVar22 = (undefined8 *)((long)puVar22 + 1);
            puVar23 = (undefined8 *)((long)puVar23 + 1);
          } while (1 < uVar15);
        }
      }
      if ((int)uVar18 < (int)uVar11) {
        uVar10 = 0;
        if (param_3 != 0) {
          uVar10 = (ulong)(long)(int)uVar11 / param_3;
        }
        FUN_ffff0000b804c464(param_1,uVar10,param_3,param_4);
      }
      iVar8 = (int)uVar20;
      if (iVar8 <= (int)uVar18) {
        return;
      }
      param_1 = (undefined8 *)((long)puVar1 - (long)iVar8);
      param_2 = 0;
      if (param_3 != 0) {
        param_2 = (ulong)(long)iVar8 / param_3;
      }
      bVar4 = (((uint)param_1 | uVar18) & 7) == 0;
      cVar7 = param_3 != 8;
      if (!bVar4) {
        cVar7 = '\x02';
      }
    } while (6 < param_2);
  }
  lVar12 = param_2 * param_3;
  if (lVar12 - param_3 != 0 && (long)param_3 <= lVar12) {
    puVar22 = (undefined8 *)((long)param_1 + param_3);
    puVar14 = param_1;
    puVar21 = puVar22;
    puVar23 = param_1;
joined_r0xffff0000b804cb04:
    do {
      if (param_1 < puVar22) {
        puVar6 = (undefined8 *)((long)puVar22 + -param_3);
        iVar8 = (*param_4)(puVar6,puVar22);
        if (0 < iVar8) {
          if (cVar7 == '\0') {
            uVar17 = *puVar22;
            *puVar22 = *puVar6;
            *puVar6 = uVar17;
          }
          else {
            lVar24 = 0;
            lVar9 = (long)(int)uVar18 + 1;
            uVar16 = ((ulong)(long)(int)uVar18 >> 3) + 1;
            if (bVar4) {
              do {
                uVar16 = uVar16 - 1;
                uVar17 = *(undefined8 *)((long)puVar14 + lVar24 + param_3);
                *(undefined8 *)((long)puVar14 + lVar24 + param_3) =
                     *(undefined8 *)((long)puVar14 + lVar24);
                *(undefined8 *)((long)puVar14 + lVar24) = uVar17;
                lVar24 = lVar24 + 8;
              } while (1 < uVar16);
            }
            else {
              do {
                lVar9 = lVar9 + -1;
                uVar2 = *(undefined1 *)((long)puVar22 + lVar24);
                *(undefined1 *)((long)puVar22 + lVar24) = *(undefined1 *)((long)puVar14 + lVar24);
                *(undefined1 *)((long)puVar14 + lVar24) = uVar2;
                lVar24 = lVar24 + 1;
              } while (1 < lVar9);
            }
          }
          puVar14 = (undefined8 *)((long)puVar14 + -param_3);
          puVar22 = puVar6;
          goto joined_r0xffff0000b804cb04;
        }
      }
      puVar22 = (undefined8 *)((long)puVar21 + param_3);
      puVar14 = (undefined8 *)((long)puVar23 + param_3);
      puVar21 = puVar22;
      puVar23 = puVar14;
    } while (puVar22 < (undefined8 *)((long)param_1 + lVar12));
  }
  return;
joined_r0xffff0000b804cc04:
  if (param_1 < puVar22) {
    puVar6 = (undefined8 *)((long)puVar22 + lVar24);
    iVar8 = (*param_4)(puVar6,puVar22);
    if (0 < iVar8) {
      if (cVar7 == '\0') {
        uVar17 = *puVar22;
        *puVar22 = *puVar6;
        *puVar6 = uVar17;
      }
      else {
        lVar12 = 0;
        lVar9 = uVar13 + 1;
        uVar16 = (uVar13 >> 3) + 1;
        if (bVar4) {
          do {
            uVar16 = uVar16 - 1;
            uVar17 = *(undefined8 *)((long)puVar14 + lVar12 + param_3);
            *(undefined8 *)((long)puVar14 + lVar12 + param_3) =
                 *(undefined8 *)((long)puVar14 + lVar12);
            *(undefined8 *)((long)puVar14 + lVar12) = uVar17;
            lVar12 = lVar12 + 8;
          } while (1 < uVar16);
        }
        else {
          do {
            lVar9 = lVar9 + -1;
            uVar2 = *(undefined1 *)((long)puVar22 + lVar12);
            *(undefined1 *)((long)puVar22 + lVar12) = *(undefined1 *)((long)puVar14 + lVar12);
            *(undefined1 *)((long)puVar14 + lVar12) = uVar2;
            lVar12 = lVar12 + 1;
          } while (1 < lVar9);
        }
      }
      puVar22 = puVar6;
      puVar14 = (undefined8 *)((long)puVar14 + lVar24);
      goto joined_r0xffff0000b804cc04;
    }
  }
  puVar22 = (undefined8 *)((long)puVar23 + param_3);
  puVar14 = (undefined8 *)((long)puVar21 + param_3);
  puVar21 = puVar14;
  puVar23 = puVar22;
  if (puVar1 <= puVar22) {
    return;
  }
  goto joined_r0xffff0000b804cc04;
}



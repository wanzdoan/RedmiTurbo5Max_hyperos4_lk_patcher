
void FUN_ffff0000b8060660
               (long param_1,long param_2,long param_3,uint *param_4,uint *param_5,uint *param_6,
               uint *param_7)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  bool bVar5;
  long lVar6;
  bool bVar7;
  long lVar8;
  long lVar9;
  long lVar10;
  ulong uVar11;
  long lVar12;
  ulong uVar13;
  int iVar14;
  uint uVar15;
  long lVar16;
  ulong uVar17;
  uint *puVar18;
  long lVar19;
  uint *puVar20;
  int iVar21;
  uint uVar22;
  uint uVar23;
  long lVar24;
  ulong uVar25;
  ulong uVar26;
  long lVar27;
  ulong uVar28;
  uint uVar29;
  long lVar30;
  long lVar31;
  int iVar32;
  uint uVar33;
  uint uVar34;
  uint uVar35;
  uint *puVar36;
  uint local_7c;
  
  lVar12 = 0;
  do {
    *(undefined8 *)((long)param_5 + lVar12) = 0;
    lVar12 = lVar12 + 8;
  } while (lVar12 != 0x300);
  lVar12 = 0;
  puVar18 = param_5;
  do {
    uVar29 = 0;
    if (*(int *)(param_3 + lVar12 * 4) != 0) {
      lVar24 = 0;
      uVar29 = 0;
      do {
        uVar23 = *(uint *)(param_3 + lVar12 * 4);
        uVar15 = *(uint *)(param_2 + lVar24) & 0xffff;
        uVar22 = *(uint *)(param_2 + lVar24) >> 0x10;
        uVar33 = uVar23 >> 0x10;
        uVar23 = uVar23 & 0xffff;
        uVar34 = uVar33 * uVar15;
        uVar1 = uVar23 * uVar22;
        uVar23 = uVar23 * uVar15;
        uVar15 = uVar34 + uVar1;
        iVar14 = 0x10000;
        if (!CARRY4(uVar34,uVar1)) {
          iVar14 = 0;
        }
        uVar34 = uVar15 * 0x10000;
        uVar1 = uVar23 + uVar34;
        uVar35 = uVar1 + uVar29;
        uVar29 = (uint)CARRY4(uVar1,uVar29) +
                 iVar14 + uVar33 * uVar22 + (uVar15 >> 0x10) + (uint)CARRY4(uVar23,uVar34) +
                 (uint)CARRY4(uVar35,*(uint *)((long)puVar18 + lVar24));
        *(uint *)((long)puVar18 + lVar24) = uVar35 + *(uint *)((long)puVar18 + lVar24);
        lVar24 = lVar24 + 4;
      } while (lVar24 != 0x180);
    }
    lVar24 = lVar12 + 1;
    puVar18 = puVar18 + 1;
    param_5[lVar12 + 0x60] = uVar29;
    lVar12 = lVar24;
  } while (lVar24 != 0x60);
  lVar12 = 0x300;
  do {
    lVar24 = lVar12 + -8;
    *(undefined8 *)((long)param_6 + lVar12 + -8) = 0;
    lVar12 = lVar24;
  } while (lVar24 != 0);
  lVar12 = 0x300;
  do {
    lVar24 = lVar12 + -8;
    *(undefined8 *)((long)param_7 + lVar12 + -8) = 0;
    lVar12 = lVar24;
  } while (lVar24 != 0);
  lVar12 = 0x5e00000000;
  uVar13 = 0x60;
  lVar24 = 0x5f00000000;
  lVar19 = 0;
  lVar6 = -0x6000000000;
  lVar9 = 0x17c;
  do {
    lVar16 = lVar9;
    lVar30 = lVar6;
    lVar27 = lVar19;
    lVar10 = lVar24;
    uVar17 = uVar13;
    lVar8 = lVar12;
    if (lVar27 == -0x60) break;
    lVar12 = lVar8 + -0x100000000;
    uVar13 = uVar17 + 1;
    lVar24 = lVar10 + -0x100000000;
    lVar19 = lVar27 + -1;
    lVar6 = lVar30 + 0x100000000;
    lVar9 = lVar16 + -4;
  } while (param_4[lVar27 + 0x5f] == 0);
  lVar24 = (long)param_7 + lVar16;
  lVar12 = lVar27 + 0x60;
  lVar19 = 0xc000000000;
  lVar6 = 0;
  do {
    lVar31 = lVar6;
    uVar13 = uVar17;
    lVar9 = lVar19;
    if (lVar31 == -0xc0) break;
    lVar19 = lVar9 + -0x100000000;
    uVar17 = uVar13 - 1;
    lVar6 = lVar31 + -1;
  } while (param_5[lVar31 + 0xbf] == 0);
  iVar14 = (int)lVar12;
  if (iVar14 == 0) goto LAB_ffff0000b80610f4;
  if (iVar14 == 1) {
    uVar29 = *param_4;
    if (uVar29 == 0) {
      *param_7 = 0;
      goto LAB_ffff0000b80610f4;
    }
    if ((int)uVar29 < 0) {
      uVar13 = 0;
      uVar23 = 0;
      uVar15 = 0;
LAB_ffff0000b80609a4:
      lVar12 = 0;
      uVar22 = 0;
      do {
        uVar33 = *(uint *)((long)param_5 + lVar12) << (ulong)(uVar23 & 0x1f) | uVar22;
        uVar22 = (*(uint *)((long)param_5 + lVar12) & ~(0xffffffffU >> (ulong)(uVar23 & 0x1f))) >>
                 (ulong)(0x20 - uVar23 & 0x1f);
        *(uint *)((long)param_6 + lVar12) = uVar33;
        lVar12 = lVar12 + 4;
        uVar33 = uVar15;
      } while (lVar12 != 0x300);
    }
    else {
      uVar17 = 0x80000000;
      uVar13 = 0;
      do {
        uVar25 = uVar13;
        uVar13 = uVar25 + 1;
        if (0x1e < uVar25) break;
        uVar17 = uVar17 >> 1;
      } while (((uint)uVar17 & uVar29) == 0);
      uVar22 = (uint)uVar13;
      uVar29 = uVar29 << (ulong)(uVar22 & 0x1f);
      if (uVar25 < 0x1f) {
        uVar15 = 0;
        uVar23 = uVar22;
        goto LAB_ffff0000b80609a4;
      }
      uVar17 = uVar13 >> 5;
      uVar25 = 0xbf;
      do {
        if (uVar25 < uVar17) {
          uVar15 = 0;
        }
        else {
          uVar15 = param_5[uVar25 - uVar17];
        }
        param_6[uVar25] = uVar15;
        uVar25 = uVar25 - 1;
      } while (uVar25 != 0xffffffffffffffff);
      uVar23 = (uint)(uVar13 & 0x1f);
      uVar15 = param_5[0xc0 - uVar17] << (ulong)(uVar22 & 0x1f);
      param_5 = param_6;
      uVar22 = uVar15;
      uVar33 = uVar23;
      if ((uVar13 & 0x1f) != 0) goto LAB_ffff0000b80609a4;
    }
    uVar22 = uVar22 | uVar33;
    lVar12 = 0x2fc;
    do {
      iVar14 = 0;
      if ((ulong)uVar29 != 0) {
        iVar14 = (int)(CONCAT44(uVar22,*(int *)((long)param_6 + lVar12)) / (ulong)uVar29);
      }
      uVar22 = *(int *)((long)param_6 + lVar12) - iVar14 * uVar29;
      *(int *)((long)param_6 + lVar12) = iVar14;
      lVar12 = lVar12 + -4;
    } while (lVar12 != -4);
    *param_7 = uVar22 >> (ulong)((uint)uVar13 & 0x1f);
    goto LAB_ffff0000b80610f4;
  }
  iVar21 = (int)lVar27;
  iVar32 = (int)lVar31;
  if ((iVar32 - iVar21) + 0x60 < 0) {
    lVar12 = 0;
    do {
      *(undefined4 *)((long)param_7 + lVar12) = *(undefined4 *)((long)param_5 + lVar12);
      lVar12 = lVar12 + 4;
    } while (lVar12 != 0x300);
    goto LAB_ffff0000b80610f4;
  }
  if (iVar32 + 0x60 == iVar21) {
    if (lVar30 == 0) {
LAB_ffff0000b8060a88:
      lVar12 = 0;
      do {
        lVar24 = lVar12 + 8;
        *(undefined8 *)((long)param_6 + lVar12 + 4) = 0;
        lVar12 = lVar24;
      } while (lVar24 != 0x2f8);
      param_6[0xbf] = 0;
      *param_6 = 1;
    }
    else {
      do {
        if (lVar16 == -4) goto LAB_ffff0000b8060a88;
        puVar18 = (uint *)((long)param_5 + lVar16);
        puVar20 = (uint *)((long)param_4 + lVar16);
        if (*puVar20 < *puVar18) goto LAB_ffff0000b8060908;
        lVar16 = lVar16 + -4;
      } while (*puVar20 <= *puVar18);
      lVar12 = 0;
      do {
        *(undefined4 *)((long)param_7 + lVar12) = *(undefined4 *)((long)param_5 + lVar12);
        lVar12 = lVar12 + 4;
      } while (lVar12 != 0x300);
    }
    goto LAB_ffff0000b80610f4;
  }
LAB_ffff0000b8060908:
  uVar17 = 0;
  uVar29 = 0x80000000;
  do {
    if ((uVar29 & *(uint *)((long)param_4 + (lVar10 >> 0x1e))) != 0) {
      local_7c = (uint)uVar17;
      uVar25 = uVar17;
      if (local_7c < 0x20) {
        bVar5 = false;
        goto joined_r0xffff0000b8060ac8;
      }
      uVar11 = uVar17 >> 5;
      goto joined_r0xffff0000b8060964;
    }
    uVar29 = uVar29 >> 1;
    uVar17 = uVar17 + 1;
  } while ((int)uVar17 != 0x20);
  local_7c = 0;
  uVar11 = 1;
  uVar25 = 0x20;
joined_r0xffff0000b8060964:
  if (lVar30 != 0) {
    uVar17 = lVar27 + 0x5f;
    do {
      if (uVar17 < uVar11) {
        uVar29 = 0;
      }
      else {
        uVar29 = param_4[uVar17 - uVar11];
      }
      param_4[uVar17] = uVar29;
      uVar17 = uVar17 - 1;
    } while (uVar17 != 0xffffffffffffffff);
  }
  uVar17 = uVar25 & 0x1f;
  if (uVar17 == 0) {
LAB_ffff0000b8060b78:
    if (lVar9 != 0) {
      uVar17 = lVar31 + 0xbf;
      do {
        if (uVar17 < uVar11) {
          uVar29 = 0;
        }
        else {
          uVar29 = param_5[uVar17 - uVar11];
        }
        param_7[uVar17] = uVar29;
        uVar17 = uVar17 - 1;
      } while (uVar17 != 0xffffffffffffffff);
    }
    uVar29 = param_5[(lVar31 + 0xc0) - uVar11] << (ulong)((uint)uVar25 & 0x1f);
    if ((uVar25 & 0x1f) != 0) {
      bVar5 = true;
      uVar15 = uVar29;
      param_5 = param_7;
      uVar23 = (uint)(uVar25 & 0x1f);
      goto joined_r0xffff0000b8060bd4;
    }
    bVar5 = true;
    uVar15 = 0;
  }
  else {
    bVar5 = true;
joined_r0xffff0000b8060ac8:
    if (lVar30 != 0) {
      uVar11 = lVar27 + 0x60;
      uVar23 = (uint)uVar17;
      uVar15 = 0x20 - uVar23;
      uVar29 = ~(0xffffffffU >> (ulong)(uVar23 & 0x1f));
      if (uVar11 < 2) {
        uVar22 = 0;
        uVar26 = 0;
      }
      else {
        uVar26 = uVar11 & 0xfffffffffffffffe;
        uVar22 = 0;
        puVar18 = param_4 + 1;
        uVar17 = uVar26;
        do {
          uVar33 = puVar18[-1];
          uVar34 = *puVar18;
          uVar17 = uVar17 - 2;
          puVar18[-1] = uVar33 << (ulong)(uVar23 & 0x1f) | uVar22;
          *puVar18 = uVar34 << (ulong)(uVar23 & 0x1f) | (uVar33 & uVar29) >> (ulong)(uVar15 & 0x1f);
          uVar22 = (uVar34 & uVar29) >> (ulong)(uVar15 & 0x1f);
          puVar18 = puVar18 + 2;
        } while (uVar17 != 0);
        if (uVar11 == uVar26) goto LAB_ffff0000b8060b6c;
      }
      lVar19 = uVar11 - uVar26;
      puVar18 = param_4 + uVar26;
      do {
        uVar33 = *puVar18;
        lVar19 = lVar19 + -1;
        *puVar18 = uVar33 << (ulong)(uVar23 & 0x1f) | uVar22;
        puVar18 = puVar18 + 1;
        uVar22 = (uVar33 & uVar29) >> (ulong)(uVar15 & 0x1f);
      } while (lVar19 != 0);
    }
LAB_ffff0000b8060b6c:
    if (bVar5) {
      uVar11 = uVar25 >> 5;
      goto LAB_ffff0000b8060b78;
    }
    bVar5 = false;
    uVar15 = 0;
    uVar23 = local_7c;
joined_r0xffff0000b8060bd4:
    uVar29 = 0;
    if (lVar9 != 0) {
      lVar19 = lVar31 + 0xc0;
      uVar29 = 0;
      puVar18 = param_7;
      do {
        lVar19 = lVar19 + -1;
        uVar22 = *param_5 << (ulong)(uVar23 & 0x1f) | uVar29;
        uVar29 = (*param_5 & ~(0xffffffffU >> (ulong)(uVar23 & 0x1f))) >>
                 (ulong)(0x20 - uVar23 & 0x1f);
        *puVar18 = uVar22;
        param_5 = param_5 + 1;
        puVar18 = puVar18 + 1;
      } while (lVar19 != 0);
    }
  }
  uVar17 = lVar27 + 0x60;
  if (-1 < (iVar32 - iVar21) + 0x60) {
    uVar29 = uVar29 | uVar15;
    uVar11 = (ulong)((iVar32 - iVar21) + 0x60);
    do {
      lVar19 = lVar27 + uVar11 + 0x5f;
      uVar15 = param_4[lVar10 >> 0x20];
      uVar23 = param_7[lVar19];
      uVar26 = 0;
      if ((ulong)uVar15 != 0) {
        uVar26 = CONCAT44(uVar29,uVar23) / (ulong)uVar15;
      }
      uVar33 = (uint)uVar26;
      uVar22 = -(uVar33 * uVar15);
      if (uVar26 >> 0x20 != 0) {
        uVar33 = 0xffffffff;
        uVar22 = uVar15;
      }
      uVar22 = uVar23 + uVar22;
      uVar34 = uVar33;
      if (((uVar26 & 0xffffffff00000000) != 0 && CARRY4(uVar15,uVar23)) || (uVar33 == 0)) {
LAB_ffff0000b8060d78:
        puVar18 = param_7 + (uVar11 & 0xffffffff);
        if (uVar34 != 0) goto joined_r0xffff0000b8060d8c;
        uVar34 = 0;
        param_6[uVar11 & 0xffffffff] = 0;
        if (uVar29 != 0) goto LAB_ffff0000b8060e3c;
      }
      else {
        uVar35 = param_4[lVar8 >> 0x20] >> 0x10;
        uVar1 = param_4[lVar8 >> 0x20] & 0xffff;
        uVar2 = uVar35 * (uVar33 & 0xffff);
        uVar3 = uVar1 * (uVar33 >> 0x10);
        uVar4 = uVar1 * (uVar33 & 0xffff);
        uVar23 = uVar2 + uVar3;
        iVar32 = 0x10000;
        iVar21 = iVar32;
        if (!CARRY4(uVar2,uVar3)) {
          iVar21 = 0;
        }
        uVar2 = uVar23 * 0x10000;
        uVar23 = iVar21 + uVar35 * (uVar33 >> 0x10) + (uVar23 >> 0x10) + (uint)CARRY4(uVar4,uVar2);
        if ((uVar22 <= uVar23) &&
           (uVar23 != uVar22 || param_7[lVar27 + uVar11 + 0x5e] < uVar4 + uVar2)) {
          uVar23 = uVar33 - 1;
          uVar34 = uVar23;
          if (!CARRY4(uVar22,uVar15)) {
            uVar3 = uVar35 * (uVar23 & 0xffff);
            uVar4 = uVar1 * (uVar23 >> 0x10);
            uVar1 = uVar1 * (uVar23 & 0xffff);
            uVar2 = uVar3 + uVar4;
            if (!CARRY4(uVar3,uVar4)) {
              iVar32 = 0;
            }
            uVar3 = uVar2 * 0x10000;
            uVar35 = iVar32 + uVar35 * (uVar23 >> 0x10) + (uVar2 >> 0x10) +
                     (uint)CARRY4(uVar1,uVar3);
            if ((uVar22 + uVar15 <= uVar35) &&
               (uVar34 = uVar33 - 2,
               uVar1 + uVar3 <= param_7[lVar27 + uVar11 + 0x5e] && uVar35 <= uVar22 + uVar15)) {
              uVar34 = uVar23;
            }
          }
          goto LAB_ffff0000b8060d78;
        }
        puVar18 = param_7 + (uVar11 & 0xffffffff);
joined_r0xffff0000b8060d8c:
        uVar15 = 0;
        if (lVar30 != 0) {
          uVar15 = 0;
          uVar26 = uVar17;
          puVar20 = puVar18;
          puVar36 = param_4;
          do {
            uVar33 = *puVar36 >> 0x10;
            uVar22 = *puVar36 & 0xffff;
            uVar1 = uVar33 * (uVar34 & 0xffff);
            uVar35 = uVar22 * (uVar34 >> 0x10);
            uVar22 = uVar22 * (uVar34 & 0xffff);
            uVar23 = uVar1 + uVar35;
            uVar2 = *puVar20 - uVar15;
            iVar21 = 0x10000;
            if (!CARRY4(uVar1,uVar35)) {
              iVar21 = 0;
            }
            uVar1 = uVar22 + uVar23 * 0x10000;
            uVar3 = uVar2 - uVar1;
            uVar35 = 2;
            if (!CARRY4(uVar15,uVar2)) {
              uVar35 = 1;
            }
            if (!CARRY4(uVar1,uVar3)) {
              uVar35 = (uint)CARRY4(uVar15,uVar2);
            }
            uVar15 = uVar33 * (uVar34 >> 0x10) + (uVar23 >> 0x10) + iVar21 + uVar35 +
                     (uint)CARRY4(uVar22,uVar23 * 0x10000);
            uVar26 = uVar26 - 1;
            *puVar20 = uVar3;
            puVar20 = puVar20 + 1;
            puVar36 = puVar36 + 1;
          } while (uVar26 != 0);
        }
        param_6[uVar11 & 0xffffffff] = uVar34;
        if (uVar29 != uVar15) {
LAB_ffff0000b8060e3c:
          param_6[uVar11 & 0xffffffff] = uVar34 - 1;
          if (lVar30 != 0) {
            uVar29 = 0;
            puVar20 = param_4;
            uVar26 = uVar17;
            do {
              uVar15 = *puVar18;
              bVar7 = CARRY4(uVar15,uVar29);
              uVar15 = uVar15 + uVar29;
              uVar29 = 2;
              if (!bVar7) {
                uVar29 = 1;
              }
              *puVar18 = uVar15;
              uVar15 = *puVar20 + uVar15;
              *puVar18 = uVar15;
              if (*puVar20 <= uVar15) {
                uVar29 = (uint)bVar7;
              }
              uVar26 = uVar26 - 1;
              puVar20 = puVar20 + 1;
              puVar18 = puVar18 + 1;
            } while (uVar26 != 0);
          }
        }
      }
      uVar29 = param_7[lVar19];
      bVar7 = 0 < (long)uVar11;
      uVar11 = uVar11 - 1;
    } while (bVar7);
  }
  if (iVar14 < (int)(lVar31 + 0xc0)) {
    uVar11 = (lVar31 - lVar27) + 0x60;
    if (1 < uVar11) {
      lVar12 = (uVar13 & 0xfffffffffffffffe) + lVar27 + 0x60;
      uVar13 = (lVar31 - lVar27) + 0x60U & 0xfffffffffffffffe;
      do {
        *(undefined8 *)(lVar24 + 4) = 0;
        uVar13 = uVar13 - 2;
        lVar24 = lVar24 + 8;
      } while (uVar13 != 0);
      if (uVar11 == (uVar11 & 0xfffffffffffffffe)) goto LAB_ffff0000b8060ef0;
    }
    do {
      param_7[lVar12] = 0;
      lVar12 = lVar12 + 1;
    } while (lVar31 + 0xc0 != lVar12);
  }
LAB_ffff0000b8060ef0:
  if (bVar5) {
    if (lVar30 != 0) {
      uVar13 = uVar25 >> 5;
      puVar18 = param_7;
      uVar11 = uVar17;
      do {
        if (uVar13 < uVar17) {
          uVar29 = puVar18[uVar25 >> 5];
        }
        else {
          uVar29 = 0;
        }
        uVar11 = uVar11 - 1;
        uVar13 = uVar13 + 1;
        *puVar18 = uVar29;
        puVar18 = puVar18 + 1;
      } while (uVar11 != 0);
      uVar13 = uVar25 & 0x1f;
      if (uVar13 != 0) {
        local_7c = (uint)uVar13;
        goto LAB_ffff0000b8060f48;
      }
      goto LAB_ffff0000b8060ff8;
    }
LAB_ffff0000b8061038:
    uVar25 = uVar25 & 0x1f;
    if (uVar25 == 0) goto LAB_ffff0000b80610f4;
  }
  else {
    uVar13 = uVar25;
    if (lVar30 == 0) goto LAB_ffff0000b80610f4;
LAB_ffff0000b8060f48:
    uVar13 = 0x20 - uVar13;
    uVar29 = ~(-1 << (ulong)(local_7c & 0x1f));
    if (uVar17 < 2) {
      uVar28 = 0;
      uVar26 = uVar17;
    }
    else {
      uVar15 = 0;
      uVar26 = uVar17 & 1;
      puVar18 = param_7 + lVar27 + 0x5f;
      uVar11 = uVar17 & 0xfffffffffffffffe;
      do {
        uVar11 = uVar11 - 2;
        uVar28 = (ulong)(puVar18[-1] & uVar29) << (uVar13 & 0x3f);
        puVar18[-1] = puVar18[-1] >> (ulong)(local_7c & 0x1f) |
                      (*puVar18 & uVar29) << (uVar13 & 0x3f);
        *puVar18 = *puVar18 >> (ulong)(local_7c & 0x1f) | uVar15;
        puVar18 = puVar18 + -2;
        uVar15 = (uint)uVar28;
      } while (uVar11 != 0);
      if (uVar17 == (uVar17 & 0xfffffffffffffffe)) goto LAB_ffff0000b8060ff8;
    }
    do {
      uVar11 = uVar26 - 1;
      uVar15 = (uint)uVar28;
      uVar28 = (ulong)(param_7[uVar26 - 1] & uVar29) << (uVar13 & 0x3f) & 0xffffffff;
      param_7[uVar26 - 1] = param_7[uVar26 - 1] >> (ulong)(local_7c & 0x1f) | uVar15;
      uVar26 = uVar11;
    } while (uVar11 != 0);
LAB_ffff0000b8060ff8:
    if (bVar5) {
      if (lVar30 != 0) {
        uVar13 = uVar25 >> 5;
        puVar18 = param_4;
        uVar11 = uVar17;
        do {
          if (uVar13 < uVar17) {
            uVar29 = puVar18[uVar25 >> 5];
          }
          else {
            uVar29 = 0;
          }
          uVar11 = uVar11 - 1;
          uVar13 = uVar13 + 1;
          *puVar18 = uVar29;
          puVar18 = puVar18 + 1;
        } while (uVar11 != 0);
      }
      goto LAB_ffff0000b8061038;
    }
  }
  if (lVar30 != 0) {
    uVar15 = (uint)uVar25;
    uVar25 = 0x20 - uVar25;
    uVar29 = ~(-1 << (ulong)(uVar15 & 0x1f));
    if (uVar17 < 2) {
      uVar26 = 0;
      uVar13 = uVar17;
    }
    else {
      uVar23 = 0;
      uVar13 = uVar17 & 1;
      puVar18 = param_4 + lVar27 + 0x5f;
      uVar11 = uVar17 & 0xfffffffffffffffe;
      do {
        uVar11 = uVar11 - 2;
        uVar26 = (ulong)(puVar18[-1] & uVar29) << (uVar25 & 0x3f);
        puVar18[-1] = puVar18[-1] >> (ulong)(uVar15 & 0x1f) | (*puVar18 & uVar29) << (uVar25 & 0x3f)
        ;
        *puVar18 = *puVar18 >> (ulong)(uVar15 & 0x1f) | uVar23;
        puVar18 = puVar18 + -2;
        uVar23 = (uint)uVar26;
      } while (uVar11 != 0);
      if (uVar17 == (uVar17 & 0xfffffffffffffffe)) goto LAB_ffff0000b80610f4;
    }
    do {
      uVar17 = uVar13 - 1;
      uVar23 = (uint)uVar26;
      uVar26 = (ulong)(param_4[uVar13 - 1] & uVar29) << (uVar25 & 0x3f) & 0xffffffff;
      param_4[uVar13 - 1] = param_4[uVar13 - 1] >> (ulong)(uVar15 & 0x1f) | uVar23;
      uVar13 = uVar17;
    } while (uVar17 != 0);
  }
LAB_ffff0000b80610f4:
  lVar12 = 0;
  do {
    *(undefined4 *)(param_1 + lVar12) = *(undefined4 *)((long)param_7 + lVar12);
    lVar12 = lVar12 + 4;
  } while (lVar12 != 0x180);
  return;
}



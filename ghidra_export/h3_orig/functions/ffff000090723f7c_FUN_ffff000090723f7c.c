// ffff000090723f7c  size=1936  FUN_ffff000090723f7c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090723f7c(uint *param_1)

{
  long lVar1;
  long *plVar2;
  long *plVar3;
  ulong *puVar4;
  uint uVar5;
  uint uVar6;
  undefined1 uVar7;
  uint uVar8;
  bool bVar9;
  int iVar10;
  int iVar11;
  ulong uVar12;
  long lVar13;
  undefined8 uVar14;
  char *pcVar15;
  long lVar16;
  long lVar17;
  long lVar18;
  long lVar19;
  long lVar20;
  long lVar21;
  undefined8 *puVar22;
  int iVar23;
  ulong uVar24;
  int *piVar25;
  long lVar26;
  ulong uVar27;
  ulong uVar28;
  ulong uVar29;
  ulong *puVar30;
  long lVar31;
  ulong *puVar32;
  
  uVar5 = *param_1;
  uVar29 = (ulong)uVar5;
  *(undefined4 *)(&DAT_ffff000090861400 + uVar29 * 4) = 0;
  FUN_ffff000090723da0(0xfff29b2f,uVar29);
  FUN_ffff000090723da0(0xfffffff6,uVar29);
  lVar17 = 0;
  lVar16 = _DAT_ffff000090861440 + (ulong)(uVar5 * 0x7d) * 0x100000;
  do {
    *(long *)((ulong)(uVar5 * 0x7d) * 8 + -0xffff6f79ebb0 + lVar17) = lVar16;
    lVar16 = lVar16 + 0x100000;
    lVar17 = lVar17 + 8;
  } while (lVar17 != 1000);
  FUN_ffff000090723da0(0xfffffff5,uVar5);
  lVar17 = 0;
  uVar8 = uVar5 * 0x4b;
  uVar24 = -(ulong)(uVar8 >> 0x1f) & 0xfffffff800000000 | (ulong)uVar8 << 3;
  do {
    *(undefined8 *)((uVar24 - 0xffff6f79cc70) + lVar17) =
         *(undefined8 *)((uVar24 - 0xffff6f79ebb0) + lVar17);
    lVar17 = lVar17 + 8;
  } while ((int)lVar17 != 600);
  FUN_ffff000090723da0(0xfffffff3,uVar5);
  FUN_ffff000090723da0(0xfffffff2,uVar5);
  uVar24 = -(ulong)(uVar5 * 0x32 >> 0x1f) & 0xfffffff800000000 | (ulong)(uVar5 * 0x32) << 3;
  lVar17 = 0;
  do {
    *(undefined8 *)((uVar24 - 0xffff6f79b9b0) + lVar17) =
         *(undefined8 *)((uVar24 - 0xffff6f79d8f0) + lVar17);
    lVar17 = lVar17 + 8;
  } while (lVar17 != 400);
  FUN_ffff000090723da0(0xfffffff1,uVar29);
  FUN_ffff000090723da0(0xfffffff0,uVar5);
  lVar17 = DAT_ffff000090839938;
  lVar16 = (long)(int)uVar8;
  do {
    iVar10 = FUN_ffff00009077d904();
    uVar24 = 0;
    iVar23 = *DAT_ffff000090839948;
    iVar11 = 0;
    if (iVar23 != 0) {
      iVar11 = iVar10 / iVar23;
    }
    uVar6 = *DAT_ffff000090839940;
    iVar23 = (iVar10 - iVar11 * iVar23) + 1;
    if (uVar6 < 2) {
      uVar6 = 1;
    }
    piVar25 = (int *)(lVar17 + 0x10);
    do {
      iVar23 = iVar23 - *piVar25;
      uVar12 = uVar24;
      if (iVar23 < 1) break;
      uVar24 = uVar24 + 1;
      uVar12 = (ulong)uVar6;
      piVar25 = piVar25 + 6;
    } while (uVar6 != uVar24);
    uVar24 = 0;
    puVar32 = *(ulong **)(lVar16 * 8 + -0xffff6f79cc70);
    lVar31 = lVar17 + uVar12 * 0x18;
    puVar30 = puVar32;
    do {
      uVar12 = FUN_ffff000090723c48(uVar24 & 0xffffffff,lVar31);
      lVar13 = FUN_ffff000090723c48((int)uVar24 + 1,lVar31);
      uVar24 = uVar24 + 2;
      *(long *)(lVar16 * 8 + -0xffff6f79ad30) = lVar31;
      *puVar30 = uVar12 & 0xffffffff | lVar13 << 0x20;
      puVar30 = puVar30 + 1;
    } while (uVar24 != 0x40000);
    uVar24 = 0;
    lVar18 = 0;
    lVar20 = 0;
    lVar13 = 1;
    lVar31 = 1;
    do {
      puVar30 = puVar32 + uVar24;
      bVar9 = uVar24 < 0x1fe;
      uVar24 = uVar24 + 2;
      uVar12 = *puVar30;
      uVar27 = puVar30[1];
      lVar21 = lVar31 + (uVar12 & 0xffffffff);
      lVar26 = lVar13 + (uVar27 & 0xffffffff);
      lVar31 = lVar21 + (uVar12 >> 0x20);
      lVar13 = lVar26 + (uVar27 >> 0x20);
      lVar20 = lVar21 + lVar20 + lVar31;
      lVar18 = lVar26 + lVar18 + lVar13;
    } while (bVar9);
    lVar21 = lVar16 * 0x20;
    bVar9 = lVar16 != (long)(int)uVar8 + 0x4a;
    *(long *)(lVar21 + -0xffff6f799a70) = lVar31;
    *(long *)(lVar21 + -0xffff6f799a68) = lVar13;
    *(long *)(lVar21 + -0xffff6f799a60) = lVar20;
    *(long *)(lVar21 + -0xffff6f799a58) = lVar18;
    lVar16 = lVar16 + 1;
  } while (bVar9);
  FUN_ffff000090723da0(0xffffffef,uVar5);
  FUN_ffff000090723da0(0xc9,uVar5);
  iVar23 = 0;
LAB_ffff00009072425c:
  FUN_ffff000090745ebc();
  _DAT_ffff000090861420 = (uint)(_DAT_ffff000090861400 < 1);
  _DAT_ffff000090861424 = (uint)(_DAT_ffff000090861404 < 1);
  _DAT_ffff000090861428 = (uint)(_DAT_ffff000090861408 < 1);
  _DAT_ffff00009086142c = (uint)(_DAT_ffff00009086140c < 1);
  _DAT_ffff000090861430 = (uint)(_DAT_ffff000090861410 < 1);
  _DAT_ffff000090861434 = (uint)(_DAT_ffff000090861414 < 1);
  uVar14 = 1;
  _DAT_ffff000090861438 = (uint)(_DAT_ffff000090861418 < 1);
  if (_DAT_ffff00009086141c < 1) {
    _DAT_ffff00009086143c = 1;
    if ((((_DAT_ffff000090861418 < 1) && (_DAT_ffff000090861414 < 1)) && (_DAT_ffff000090861410 < 1)
        ) && (((_DAT_ffff00009086140c < 1 && (_DAT_ffff000090861408 < 1)) &&
              ((_DAT_ffff000090861404 < 1 && (_DAT_ffff000090861400 < 1)))))) {
      iVar11 = FUN_ffff00009077d904();
      uVar24 = (uVar29 + (long)iVar11) % 600;
      plVar2 = (long *)(uVar24 * 8 + -0xffff6f794f70);
      lVar17 = *plVar2;
      while (lVar17 != 0) {
        iVar11 = FUN_ffff00009077d904();
        uVar24 = (uVar29 + (long)iVar11) % 600;
        plVar2 = (long *)(uVar24 * 8 + -0xffff6f794f70);
        lVar17 = *plVar2;
      }
      puVar30 = *(ulong **)(uVar24 * 8 + -0xffff6f79cc70);
      *(undefined8 *)(uVar24 * 8 + -0xffff6f794f70) = 1;
      iVar11 = FUN_ffff00009077d904();
      while( true ) {
        uVar12 = (uVar29 + (long)iVar11) % 400;
        plVar3 = (long *)(uVar12 * 8 + -0xffff6f793cb0);
        if (*plVar3 == 0) break;
        iVar11 = FUN_ffff00009077d904();
      }
      lVar20 = uVar12 * 8;
      lVar31 = 0;
      lVar17 = uVar24 * 0x20;
      lVar16 = 0;
      lVar13 = *(long *)(lVar20 + -0xffff6f79b9b0);
      *(undefined8 *)(lVar20 + -0xffff6f793cb0) = 1;
LAB_ffff0000907243fc:
      lVar19 = 0;
      lVar21 = 0;
      uVar12 = 0xfffffffffffffffe;
      lVar26 = 1;
      lVar18 = 1;
      lVar20 = lVar31;
      do {
        puVar32 = (ulong *)((long)puVar30 + lVar20);
        puVar4 = (ulong *)(lVar13 + lVar20);
        uVar12 = uVar12 + 2;
        lVar20 = lVar20 + 0x10;
        uVar28 = *puVar32;
        *puVar4 = uVar28;
        lVar1 = lVar18 + (uVar28 & 0xffffffff);
        uVar27 = puVar32[1];
        lVar18 = lVar1 + (uVar28 >> 0x20);
        lVar21 = lVar1 + lVar21 + lVar18;
        lVar1 = lVar26 + (uVar27 & 0xffffffff);
        puVar4[1] = uVar27;
        lVar26 = lVar1 + (uVar27 >> 0x20);
        lVar19 = lVar1 + lVar19 + lVar26;
      } while (uVar12 < 0x1fe);
      if (((lVar18 == *(long *)(lVar17 + -0xffff6f799a70)) &&
          (lVar26 == *(long *)(lVar17 + -0xffff6f799a68))) &&
         ((lVar21 == *(long *)(lVar17 + -0xffff6f799a60) &&
          (lVar19 == *(long *)(lVar17 + -0xffff6f799a58))))) goto code_r0xffff00009072448c;
      puVar22 = *(undefined8 **)(uVar24 * 8 + -0xffff6f79ad30);
      uVar7 = *(undefined1 *)((long)puVar22 + 0xc);
      uVar14 = *(undefined8 *)*puVar22;
      *(int *)(&DAT_ffff000090861400 + uVar29 * 4) =
           *(int *)(&DAT_ffff000090861400 + uVar29 * 4) + 1;
      FUN_ffff00009077de54
                (s__PATTERNFAIL___d__Invert_false_p_ffff0000907f5ffe,uVar5,uVar24,lVar16,uVar14,
                 *(undefined4 *)(puVar22 + 1),uVar7);
      lVar17 = 0;
      iVar11 = 0;
      do {
        FUN_ffff00009077cbd8(100);
        uVar12 = *(ulong *)((long)puVar30 + lVar17 + lVar31);
        uVar24 = *(ulong *)(lVar13 + lVar31 + lVar17);
        if (uVar12 != uVar24) {
          iVar11 = iVar11 + 1;
          FUN_ffff00009077de54
                    (s__MISCOMPARE___d__PA__0x_llx__tar_ffff0000907ddc0d,uVar5,
                     lVar13 + lVar31 + lVar17,uVar24 >> 0x20,uVar24,uVar12 >> 0x20);
        }
        lVar17 = lVar17 + 8;
      } while (lVar17 != 0x1000);
      if (iVar11 == 0) {
        pcVar15 = s__PATTERNFAIL___d__COPY_THREAD_CR_ffff0000907fb3b7;
        goto LAB_ffff000090724618;
      }
      goto LAB_ffff000090724228;
    }
  }
  else {
    _DAT_ffff00009086143c = 0;
  }
LAB_ffff0000907246e4:
  *(undefined4 *)(&DAT_ffff000090861400 + uVar29 * 4) = 0;
  return uVar14;
code_r0xffff00009072448c:
  lVar16 = lVar16 + 1;
  lVar31 = lVar31 + 0x1000;
  if (lVar16 == 0x100) goto code_r0xffff00009072449c;
  goto LAB_ffff0000907243fc;
code_r0xffff00009072449c:
  FUN_ffff000090724a5c(lVar13);
  FUN_ffff000090724bac(lVar13);
  FUN_ffff000090724bac(lVar13);
  FUN_ffff000090724a5c(lVar13);
  lVar16 = 0;
  lVar31 = lVar13;
  while( true ) {
    uVar12 = 0;
    lVar26 = 0;
    lVar21 = 0;
    lVar18 = 1;
    lVar20 = 1;
    do {
      puVar32 = (ulong *)(lVar13 + uVar12 * 8);
      bVar9 = uVar12 < 0x1fe;
      uVar12 = uVar12 + 2;
      uVar27 = *puVar32;
      uVar28 = puVar32[1];
      lVar19 = lVar20 + (uVar27 & 0xffffffff);
      lVar1 = lVar18 + (uVar28 & 0xffffffff);
      lVar20 = lVar19 + (uVar27 >> 0x20);
      lVar18 = lVar1 + (uVar28 >> 0x20);
      lVar21 = lVar19 + lVar21 + lVar20;
      lVar26 = lVar1 + lVar26 + lVar18;
    } while (bVar9);
    if ((((lVar20 != *(long *)(lVar17 + -0xffff6f799a70)) ||
         (lVar18 != *(long *)(lVar17 + -0xffff6f799a68))) ||
        (lVar21 != *(long *)(lVar17 + -0xffff6f799a60))) ||
       (lVar26 != *(long *)(lVar17 + -0xffff6f799a58))) break;
    lVar16 = lVar16 + 1;
    lVar13 = lVar13 + 0x1000;
    lVar31 = lVar31 + 0x1000;
    puVar30 = puVar30 + 0x200;
    if (lVar16 == 0x100) goto LAB_ffff000090724228;
  }
  puVar22 = *(undefined8 **)(uVar24 * 8 + -0xffff6f79ad30);
  uVar7 = *(undefined1 *)((long)puVar22 + 0xc);
  uVar14 = *(undefined8 *)*puVar22;
  *(int *)(&DAT_ffff000090861400 + uVar29 * 4) = *(int *)(&DAT_ffff000090861400 + uVar29 * 4) + 1;
  FUN_ffff00009077de54
            (s__PATTERNFAIL___d__Invert__true_p_ffff0000907e7648,uVar5,uVar24,lVar16,uVar14,
             *(undefined4 *)(puVar22 + 1),uVar7);
  lVar17 = 0;
  iVar11 = 0;
  do {
    FUN_ffff00009077cbd8(100);
    uVar24 = *(ulong *)(lVar13 + lVar17);
    if (*puVar30 != uVar24) {
      iVar11 = iVar11 + 1;
      FUN_ffff00009077de54
                (s__MISCOMPARE___d__PA__0x_llx__tar_ffff0000907ddc0d,uVar5,lVar31,uVar24 >> 0x20,
                 uVar24,*puVar30 >> 0x20);
    }
    lVar17 = lVar17 + 8;
    lVar31 = lVar31 + 8;
    puVar30 = puVar30 + 1;
  } while (lVar17 != 0x1000);
  if (iVar11 == 0) {
    pcVar15 = s__PATTERNFAIL___d__INVERT_THREAD__ffff0000907ef53e;
LAB_ffff000090724618:
    FUN_ffff00009077de54(pcVar15,uVar5);
  }
LAB_ffff000090724228:
  iVar23 = iVar23 + 1;
  *plVar2 = 0;
  *plVar3 = 0;
  if (iVar23 == 0x20) {
    uVar14 = 0;
    *(undefined4 *)(&DAT_ffff000090861420 + uVar29 * 4) = 1;
    goto LAB_ffff0000907246e4;
  }
  goto LAB_ffff00009072425c;
}



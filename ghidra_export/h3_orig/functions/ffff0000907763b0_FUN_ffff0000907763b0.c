// ffff0000907763b0  size=7696  FUN_ffff0000907763b0

int FUN_ffff0000907763b0(long *param_1,int param_2)

{
  uint uVar1;
  undefined8 *puVar2;
  undefined8 *puVar3;
  undefined8 *puVar4;
  undefined8 *puVar5;
  uint uVar6;
  uint uVar7;
  byte bVar8;
  byte bVar9;
  ushort uVar10;
  uint uVar11;
  ushort uVar12;
  bool bVar13;
  int iVar14;
  undefined8 uVar15;
  ulong uVar16;
  byte *pbVar17;
  uint uVar18;
  ulong uVar19;
  uint uVar20;
  undefined4 uVar21;
  int iVar22;
  long lVar23;
  undefined1 *puVar24;
  ulong uVar25;
  char *pcVar26;
  byte *pbVar27;
  long lVar28;
  uint uVar29;
  ulong uVar30;
  int iVar31;
  ushort *puVar32;
  undefined2 uVar33;
  byte *pbVar34;
  ulong uVar35;
  byte *pbVar36;
  undefined1 *puVar37;
  undefined1 *puVar38;
  uint uVar39;
  uint uVar40;
  ulong uVar41;
  undefined8 *puVar42;
  uint uVar43;
  uint uVar44;
  ulong uVar45;
  int local_7c;
  undefined4 local_64;
  
  local_64 = 0;
  if ((((((param_1 == (long *)0x0) || (param_1[8] == 0)) || (param_1[9] == 0)) ||
       ((puVar42 = (undefined8 *)param_1[7], puVar42 == (undefined8 *)0x0 ||
        ((long *)*puVar42 != param_1)))) ||
      ((iVar14 = *(int *)(puVar42 + 1), 0x1f < iVar14 - 0x3f34U ||
       (puVar37 = (undefined1 *)param_1[3], puVar37 == (undefined1 *)0x0)))) ||
     ((pbVar17 = (byte *)*param_1, pbVar17 == (byte *)0x0 && ((int)param_1[1] != 0)))) {
LAB_ffff000090777f24:
    return -2;
  }
  if (iVar14 == 0x3f3f) {
    iVar14 = 0x3f40;
    *(undefined4 *)(puVar42 + 1) = 0x3f40;
  }
  puVar2 = puVar42 + 0xab;
  puVar3 = puVar42 + 0x12;
  uVar39 = *(uint *)(param_1 + 4);
  puVar4 = puVar42 + 99;
  uVar7 = *(uint *)(param_1 + 1);
  puVar5 = puVar42 + 0x13;
  uVar45 = puVar42[10];
  uVar19 = (ulong)*(uint *)(puVar42 + 0xb);
  uVar41 = (ulong)uVar7;
  local_7c = 0;
  uVar40 = uVar39;
LAB_ffff000090776498:
  uVar18 = (uint)uVar19;
  uVar20 = (uint)uVar41;
  pbVar27 = pbVar17;
  uVar16 = uVar41;
  iVar31 = 1;
  iVar22 = -3;
  pbVar36 = pbVar17;
  uVar44 = uVar18;
  switch(iVar14) {
  case 0x3f34:
    uVar43 = *(uint *)(puVar42 + 2);
    if (uVar43 == 0) {
      uVar21 = 0x3f40;
      goto LAB_ffff000090777ee4;
    }
    if (uVar18 < 0x10) {
      if (uVar20 != 0) {
        uVar29 = uVar20 - 1;
        pbVar27 = pbVar17 + 1;
        uVar45 = ((ulong)*pbVar17 << (uVar19 & 0x3f)) + uVar45;
        uVar16 = uVar19 + 8;
        if (uVar18 < 8) {
          pbVar36 = pbVar27;
          uVar44 = (uint)uVar16;
          if (uVar29 == 0) goto LAB_ffff000090778038;
          uVar29 = uVar20 - 2;
          pbVar27 = pbVar17 + 2;
          uVar45 = ((ulong)pbVar17[1] << (uVar16 & 0x3f)) + uVar45;
          uVar16 = uVar19 + 0x10;
        }
        uVar41 = (ulong)uVar29;
        uVar19 = uVar16 & 0xffffffff;
        goto LAB_ffff000090776520;
      }
      goto LAB_ffff000090778038;
    }
LAB_ffff000090776520:
    if (((uVar43 >> 1 & 1) == 0) || (uVar45 != 0x8b1f)) {
      if (puVar42[6] != 0) {
        *(undefined4 *)(puVar42[6] + 0x48) = 0xffffffff;
      }
      if (((uVar43 & 1) == 0) ||
         (0x842108421084210 < ((uVar45 & 0xff) * 0x100 + (uVar45 >> 8)) * -0x1084210842108421)) {
        pcVar26 = s_incorrect_header_check_ffff0000907e534f;
        goto LAB_ffff000090777edc;
      }
      if ((uVar45 & 0xf) != 8) goto LAB_ffff000090777ed4;
      uVar16 = uVar45 >> 4 & 0xf;
      uVar20 = (uint)uVar16;
      uVar18 = uVar20 + 8;
      uVar44 = *(uint *)(puVar42 + 7);
      if (*(uint *)(puVar42 + 7) == 0) {
        *(uint *)(puVar42 + 7) = uVar18;
        uVar44 = uVar18;
      }
      if ((uVar20 < 8) && (uVar18 <= uVar44)) {
        *(undefined4 *)(puVar42 + 3) = 0;
        *(int *)((long)puVar42 + 0x1c) = 0x100 << uVar16;
        lVar28 = FUN_ffff00009076c008(0,0,0);
        uVar19 = 0;
        uVar21 = 0x3f3f;
        if ((uVar45 & 0x2000) != 0) {
          uVar21 = 0x3f3d;
        }
        puVar42[4] = lVar28;
        uVar45 = 0;
        param_1[0xc] = lVar28;
      }
      else {
        uVar19 = (ulong)((int)uVar19 - 4);
        uVar45 = uVar45 >> 4;
        uVar21 = 0x3f51;
        param_1[6] = (long)s_invalid_window_size_ffff0000907d0168;
      }
    }
    else {
      if (*(int *)(puVar42 + 7) == 0) {
        *(undefined4 *)(puVar42 + 7) = 0xf;
      }
      uVar15 = FUN_ffff00009076c7c4(0,0,0);
      puVar42[4] = uVar15;
      local_64 = CONCAT22(local_64._2_2_,0x8b1f);
      uVar15 = FUN_ffff00009076c7c4(uVar15,&local_64,2);
      uVar45 = 0;
      uVar19 = 0;
      uVar21 = 0x3f35;
      puVar42[4] = uVar15;
    }
    *(undefined4 *)(puVar42 + 1) = uVar21;
    goto LAB_ffff000090777a20;
  case 0x3f35:
    if (uVar18 < 0x10) {
      if (uVar20 == 0) break;
      uVar43 = uVar20 - 1;
      pbVar27 = pbVar17 + 1;
      uVar45 = ((ulong)*pbVar17 << (uVar19 & 0x3f)) + uVar45;
      uVar16 = uVar19 + 8;
      if (uVar18 < 8) {
        uVar44 = (uint)uVar16;
        if (uVar43 == 0) break;
        uVar43 = uVar20 - 2;
        pbVar27 = pbVar17 + 2;
        uVar45 = ((ulong)pbVar17[1] << (uVar16 & 0x3f)) + uVar45;
        uVar16 = uVar19 + 0x10;
      }
      uVar41 = (ulong)uVar43;
      uVar19 = uVar16 & 0xffffffff;
    }
    uVar20 = (uint)uVar41;
    uVar18 = (uint)uVar45;
    *(uint *)(puVar42 + 3) = uVar18;
    if ((uVar18 & 0xff) == 8) {
      if ((uVar45 & 0xe000) == 0) {
        if ((uint *)puVar42[6] != (uint *)0x0) {
          *(uint *)puVar42[6] = uVar18 >> 8 & 1;
        }
        if (((uVar18 >> 9 & 1) != 0) && ((*(byte *)(puVar42 + 2) >> 2 & 1) != 0)) {
          local_64 = CONCAT22(local_64._2_2_,(short)uVar45);
          uVar15 = FUN_ffff00009076c7c4(puVar42[4],&local_64,2);
          puVar42[4] = uVar15;
        }
        uVar19 = 0;
        uVar45 = 0;
        *(undefined4 *)(puVar42 + 1) = 0x3f36;
        goto LAB_ffff000090777190;
      }
      pcVar26 = s_unknown_header_flags_set_ffff0000907ed02d;
    }
    else {
LAB_ffff000090777ed4:
      pcVar26 = s_unknown_compression_method_ffff0000907e38ed;
    }
    goto LAB_ffff000090777edc;
  case 0x3f36:
    uVar44 = uVar20;
    if (uVar18 < 0x20) {
LAB_ffff000090777190:
      uVar43 = (uint)uVar19;
      pbVar36 = pbVar27;
      uVar18 = uVar43;
      if (uVar20 == 0) {
LAB_ffff00009077805c:
        pbVar17 = pbVar36;
        uVar20 = 0;
        iVar31 = local_7c;
        goto code_r0xffff000090778068;
      }
      pbVar17 = pbVar27 + 1;
      uVar45 = ((ulong)*pbVar27 << (uVar19 & 0x3f)) + uVar45;
      uVar44 = uVar20 - 1;
      if (uVar43 < 0x18) {
        uVar18 = (uint)(uVar19 + 8);
        pbVar36 = pbVar17;
        if (uVar20 - 1 == 0) goto LAB_ffff00009077805c;
        pbVar17 = pbVar27 + 2;
        uVar45 = ((ulong)pbVar27[1] << (uVar19 + 8 & 0x3f)) + uVar45;
        uVar44 = uVar20 - 2;
        if (uVar43 < 0x10) {
          uVar18 = (uint)(uVar19 + 0x10);
          pbVar36 = pbVar17;
          if (uVar20 - 2 == 0) goto LAB_ffff00009077805c;
          pbVar17 = pbVar27 + 3;
          uVar45 = ((ulong)pbVar27[2] << (uVar19 + 0x10 & 0x3f)) + uVar45;
          uVar44 = uVar20 - 3;
          if (uVar43 < 8) {
            pbVar36 = pbVar17;
            uVar18 = (uint)(uVar19 + 0x18);
            if (uVar20 - 3 == 0) goto LAB_ffff00009077805c;
            pbVar17 = pbVar27 + 4;
            uVar45 = ((ulong)pbVar27[3] << (uVar19 + 0x18 & 0x3f)) + uVar45;
            uVar44 = uVar20 - 4;
          }
        }
      }
    }
    uVar20 = uVar44;
    if (puVar42[6] != 0) {
      *(ulong *)(puVar42[6] + 8) = uVar45;
    }
    if (((*(byte *)((long)puVar42 + 0x19) >> 1 & 1) != 0) &&
       ((*(byte *)(puVar42 + 2) >> 2 & 1) != 0)) {
      local_64 = (undefined4)uVar45;
      uVar15 = FUN_ffff00009076c7c4(puVar42[4],&local_64,4);
      puVar42[4] = uVar15;
    }
    uVar19 = 0;
    uVar45 = 0;
    *(undefined4 *)(puVar42 + 1) = 0x3f37;
    pbVar36 = pbVar17;
LAB_ffff000090777288:
    pbVar27 = pbVar36;
    uVar44 = (uint)uVar19;
    if (uVar20 != 0) {
      pbVar17 = pbVar36 + 1;
      uVar45 = ((ulong)*pbVar36 << (uVar19 & 0x3f)) + uVar45;
      if ((uint)uVar19 < 8) {
        pbVar27 = pbVar17;
        uVar44 = (uint)(uVar19 + 8);
        if (uVar20 - 1 == 0) break;
        uVar41 = (ulong)(uVar20 - 2);
        pbVar17 = pbVar36 + 2;
        uVar45 = ((ulong)pbVar36[1] << (uVar19 + 8 & 0x3f)) + uVar45;
      }
      else {
        uVar41 = (ulong)(uVar20 - 1);
      }
      goto LAB_ffff0000907772dc;
    }
    break;
  case 0x3f37:
    if (uVar18 < 0x10) goto LAB_ffff000090777288;
LAB_ffff0000907772dc:
    uVar20 = (uint)uVar41;
    lVar28 = puVar42[6];
    if (lVar28 != 0) {
      *(uint *)(lVar28 + 0x10) = (uint)uVar45 & 0xff;
      *(int *)(lVar28 + 0x14) = (int)(uVar45 >> 8);
    }
    if (((*(byte *)((long)puVar42 + 0x19) >> 1 & 1) != 0) &&
       ((*(byte *)(puVar42 + 2) >> 2 & 1) != 0)) {
      local_64 = CONCAT22(local_64._2_2_,(short)uVar45);
      uVar15 = FUN_ffff00009076c7c4(puVar42[4],&local_64,2);
      puVar42[4] = uVar15;
    }
    uVar45 = 0;
    uVar43 = *(uint *)(puVar42 + 3);
    uVar19 = 0;
    *(undefined4 *)(puVar42 + 1) = 0x3f38;
    pbVar27 = pbVar17;
    uVar16 = 0;
    if ((uVar43 >> 10 & 1) == 0) goto LAB_ffff00009077734c;
LAB_ffff00009077735c:
    pbVar36 = pbVar27;
    uVar44 = (uint)uVar16;
    if (uVar20 == 0) goto LAB_ffff000090778038;
    pbVar17 = pbVar27 + 1;
    uVar45 = ((ulong)*pbVar27 << (uVar16 & 0x3f)) + uVar45;
    if ((uint)uVar16 < 8) {
      pbVar36 = pbVar17;
      uVar44 = (uint)(uVar16 + 8);
      if (uVar20 - 1 == 0) goto LAB_ffff000090778038;
      uVar41 = (ulong)(uVar20 - 2);
      pbVar17 = pbVar27 + 2;
      uVar45 = ((ulong)pbVar27[1] << (uVar16 + 8 & 0x3f)) + uVar45;
    }
    else {
      uVar41 = (ulong)(uVar20 - 1);
    }
LAB_ffff0000907773b0:
    *(int *)((long)puVar42 + 0x5c) = (int)uVar45;
    if (puVar42[6] != 0) {
      *(int *)(puVar42[6] + 0x20) = (int)uVar45;
    }
    if (((uVar43 >> 9 & 1) == 0) || ((*(byte *)(puVar42 + 2) >> 2 & 1) == 0)) {
      uVar45 = 0;
      uVar19 = 0;
    }
    else {
      local_64 = CONCAT22(local_64._2_2_,(short)uVar45);
      uVar15 = FUN_ffff00009076c7c4(puVar42[4],&local_64,2);
      uVar45 = 0;
      uVar19 = 0;
      puVar42[4] = uVar15;
    }
    goto LAB_ffff000090777410;
  case 0x3f38:
    uVar43 = *(uint *)(puVar42 + 3);
    if ((uVar43 >> 10 & 1) != 0) {
      pbVar27 = pbVar17;
      uVar16 = uVar19;
      if (uVar18 < 0x10) goto LAB_ffff00009077735c;
      goto LAB_ffff0000907773b0;
    }
LAB_ffff00009077734c:
    if (puVar42[6] != 0) {
      *(undefined8 *)(puVar42[6] + 0x18) = 0;
    }
LAB_ffff000090777410:
    *(undefined4 *)(puVar42 + 1) = 0x3f39;
code_r0xffff000090777420:
    uVar18 = *(uint *)(puVar42 + 3);
    if ((uVar18 >> 10 & 1) != 0) {
      uVar43 = *(uint *)((long)puVar42 + 0x5c);
      uVar20 = (uint)uVar41;
      uVar44 = uVar43;
      if (uVar20 <= uVar43) {
        uVar44 = uVar20;
      }
      if (uVar44 != 0) {
        lVar28 = puVar42[6];
        if ((lVar28 != 0) && (*(long *)(lVar28 + 0x18) != 0)) {
          uVar29 = *(uint *)(lVar28 + 0x24);
          uVar43 = *(int *)(lVar28 + 0x20) - uVar43;
          if (uVar43 <= uVar29 && uVar29 - uVar43 != 0) {
            uVar18 = uVar29 - uVar43;
            if (uVar43 + uVar44 <= uVar29) {
              uVar18 = uVar44;
            }
            FUN_ffff00009077e858(*(long *)(lVar28 + 0x18) + (ulong)uVar43,pbVar17,uVar18);
            uVar18 = *(uint *)(puVar42 + 3);
          }
        }
        if (((uVar18 >> 9 & 1) != 0) && ((*(byte *)(puVar42 + 2) >> 2 & 1) != 0)) {
          uVar15 = FUN_ffff00009076c7c4(puVar42[4],pbVar17,uVar44);
          puVar42[4] = uVar15;
        }
        uVar41 = (ulong)(uVar20 - uVar44);
        pbVar17 = pbVar17 + uVar44;
        uVar43 = *(int *)((long)puVar42 + 0x5c) - uVar44;
        *(uint *)((long)puVar42 + 0x5c) = uVar43;
      }
      uVar20 = (uint)uVar41;
      if (uVar43 != 0) goto LAB_ffff000090777f64;
    }
    *(undefined4 *)((long)puVar42 + 0x5c) = 0;
    *(undefined4 *)(puVar42 + 1) = 0x3f3a;
code_r0xffff00009077751c:
    uVar18 = (uint)uVar19;
    if ((*(byte *)((long)puVar42 + 0x19) >> 3 & 1) == 0) {
      if (puVar42[6] != 0) {
        *(undefined8 *)(puVar42[6] + 0x28) = 0;
      }
    }
    else {
      uVar20 = 0;
      if ((int)uVar41 == 0) goto LAB_ffff000090777f64;
      uVar16 = 0;
      do {
        bVar8 = pbVar17[uVar16];
        lVar28 = puVar42[6];
        if ((lVar28 != 0) && (lVar23 = *(long *)(lVar28 + 0x28), lVar23 != 0)) {
          uVar44 = *(uint *)((long)puVar42 + 0x5c);
          if (uVar44 < *(uint *)(lVar28 + 0x30)) {
            *(uint *)((long)puVar42 + 0x5c) = uVar44 + 1;
            *(byte *)(lVar23 + (ulong)uVar44) = bVar8;
          }
        }
        uVar16 = uVar16 + 1;
      } while ((bVar8 != 0) && (uVar16 < (uVar41 & 0xffffffff)));
      if (((*(byte *)((long)puVar42 + 0x19) >> 1 & 1) != 0) &&
         ((*(byte *)(puVar42 + 2) >> 2 & 1) != 0)) {
        uVar15 = FUN_ffff00009076c7c4(puVar42[4],pbVar17,uVar16 & 0xffffffff);
        puVar42[4] = uVar15;
      }
      pbVar17 = pbVar17 + uVar16;
      if (bVar8 != 0) {
        uVar20 = (int)uVar41 - (int)uVar16;
        iVar31 = local_7c;
        goto code_r0xffff000090778068;
      }
      uVar41 = (uVar41 & 0xffffffff) - uVar16;
    }
    *(undefined4 *)((long)puVar42 + 0x5c) = 0;
    *(undefined4 *)(puVar42 + 1) = 0x3f3b;
code_r0xffff0000907775f8:
    uVar18 = (uint)uVar19;
    if ((*(byte *)((long)puVar42 + 0x19) >> 4 & 1) == 0) {
      if (puVar42[6] != 0) {
        *(undefined8 *)(puVar42[6] + 0x38) = 0;
      }
    }
    else {
      uVar20 = 0;
      if ((int)uVar41 == 0) goto LAB_ffff000090777f64;
      uVar16 = 0;
      do {
        bVar8 = pbVar17[uVar16];
        lVar28 = puVar42[6];
        if ((lVar28 != 0) && (lVar23 = *(long *)(lVar28 + 0x38), lVar23 != 0)) {
          uVar44 = *(uint *)((long)puVar42 + 0x5c);
          if (uVar44 < *(uint *)(lVar28 + 0x40)) {
            *(uint *)((long)puVar42 + 0x5c) = uVar44 + 1;
            *(byte *)(lVar23 + (ulong)uVar44) = bVar8;
          }
        }
        uVar16 = uVar16 + 1;
      } while ((bVar8 != 0) && (uVar16 < (uVar41 & 0xffffffff)));
      if (((*(byte *)((long)puVar42 + 0x19) >> 1 & 1) != 0) &&
         ((*(byte *)(puVar42 + 2) >> 2 & 1) != 0)) {
        uVar15 = FUN_ffff00009076c7c4(puVar42[4],pbVar17,uVar16 & 0xffffffff);
        puVar42[4] = uVar15;
      }
      pbVar17 = pbVar17 + uVar16;
      if (bVar8 != 0) {
        uVar20 = (int)uVar41 - (int)uVar16;
        iVar31 = local_7c;
        goto code_r0xffff000090778068;
      }
      uVar41 = (uVar41 & 0xffffffff) - uVar16 & 0xffffffff;
    }
    *(undefined4 *)(puVar42 + 1) = 0x3f3c;
    pbVar27 = pbVar17;
code_r0xffff0000907776d8:
    uVar44 = (uint)uVar19;
    if ((*(uint *)(puVar42 + 3) >> 9 & 1) != 0) {
      if (uVar44 < 0x10) {
        iVar14 = (int)uVar41;
        pbVar36 = pbVar27;
        if (iVar14 == 0) {
LAB_ffff000090778038:
          uVar18 = uVar44;
          pbVar17 = pbVar36;
          uVar20 = 0;
          iVar31 = local_7c;
          goto code_r0xffff000090778068;
        }
        uVar18 = iVar14 - 1;
        uVar45 = ((ulong)*pbVar27 << (uVar19 & 0x3f)) + uVar45;
        uVar16 = uVar19 + 8;
        pbVar17 = pbVar27 + 1;
        if (uVar44 < 8) {
          pbVar36 = pbVar27 + 1;
          uVar44 = (uint)uVar16;
          if (uVar18 == 0) goto LAB_ffff000090778038;
          uVar18 = iVar14 - 2;
          uVar45 = ((ulong)pbVar27[1] << (uVar16 & 0x3f)) + uVar45;
          uVar16 = uVar19 + 0x10;
          pbVar17 = pbVar27 + 2;
        }
        pbVar27 = pbVar17;
        uVar41 = (ulong)uVar18;
        uVar19 = uVar16 & 0xffffffff;
      }
      if (((*(byte *)(puVar42 + 2) >> 2 & 1) != 0) && (uVar45 != *(ushort *)(puVar42 + 4))) {
        pcVar26 = s_header_crc_mismatch_ffff0000908014ca;
LAB_ffff000090777edc:
        uVar21 = 0x3f51;
        param_1[6] = (long)pcVar26;
        pbVar17 = pbVar27;
LAB_ffff000090777ee4:
        *(undefined4 *)(puVar42 + 1) = uVar21;
LAB_ffff000090777ee8:
        iVar14 = *(int *)(puVar42 + 1);
        goto LAB_ffff000090776498;
      }
      uVar45 = 0;
      uVar19 = 0;
    }
    lVar28 = puVar42[6];
    if (lVar28 != 0) {
      *(uint *)(lVar28 + 0x44) = *(uint *)(puVar42 + 3) >> 9 & 1;
      *(undefined4 *)(lVar28 + 0x48) = 1;
    }
    lVar28 = FUN_ffff00009076c7c4(0,0,0);
    iVar14 = 0x3f3f;
    puVar42[4] = lVar28;
    param_1[0xc] = lVar28;
    pbVar17 = pbVar27;
LAB_ffff00009077778c:
    *(int *)(puVar42 + 1) = iVar14;
    goto LAB_ffff000090776498;
  case 0x3f39:
    goto code_r0xffff000090777420;
  case 0x3f3a:
    goto code_r0xffff00009077751c;
  case 0x3f3b:
    goto code_r0xffff0000907775f8;
  case 0x3f3c:
    goto code_r0xffff0000907776d8;
  case 0x3f3d:
    if (0x1f < uVar18) {
LAB_ffff000090776eb8:
      uVar19 = 0;
      uVar18 = ((uint)uVar45 & 0xff00ff00) >> 8 | ((uint)uVar45 & 0xff00ff) << 8;
      uVar16 = (ulong)(uVar18 >> 0x10 | uVar18 << 0x10);
      uVar45 = 0;
      puVar42[4] = uVar16;
      param_1[0xc] = uVar16;
      *(undefined4 *)(puVar42 + 1) = 0x3f3e;
      goto code_r0xffff000090776ed8;
    }
    if (uVar20 != 0) {
      uVar43 = uVar20 - 1;
      uVar45 = ((ulong)*pbVar17 << (uVar19 & 0x3f)) + uVar45;
      pbVar27 = pbVar17 + 1;
      if (0x17 < uVar18) {
LAB_ffff000090776eb0:
        pbVar17 = pbVar27;
        uVar41 = (ulong)uVar43;
        goto LAB_ffff000090776eb8;
      }
      uVar44 = (uint)(uVar19 + 8);
      if (uVar43 != 0) {
        uVar43 = uVar20 - 2;
        uVar45 = ((ulong)pbVar17[1] << (uVar19 + 8 & 0x3f)) + uVar45;
        pbVar27 = pbVar17 + 2;
        if (0xf < uVar18) goto LAB_ffff000090776eb0;
        uVar44 = (uint)(uVar19 + 0x10);
        if (uVar43 != 0) {
          uVar43 = uVar20 - 3;
          uVar45 = ((ulong)pbVar17[2] << (uVar19 + 0x10 & 0x3f)) + uVar45;
          pbVar27 = pbVar17 + 3;
          if (7 < uVar18) goto LAB_ffff000090776eb0;
          uVar44 = (uint)(uVar19 + 0x18);
          if (uVar43 != 0) {
            pbVar27 = pbVar17 + 3;
            uVar41 = (ulong)(uVar20 - 4);
            pbVar17 = pbVar17 + 4;
            uVar45 = ((ulong)*pbVar27 << (uVar19 + 0x18 & 0x3f)) + uVar45;
            goto LAB_ffff000090776eb8;
          }
        }
      }
    }
    break;
  case 0x3f3e:
code_r0xffff000090776ed8:
    if (*(int *)((long)puVar42 + 0x14) == 0) {
      param_1[3] = (long)puVar37;
      *(uint *)(param_1 + 4) = uVar39;
      *param_1 = (long)pbVar17;
      *(int *)(param_1 + 1) = (int)uVar41;
      puVar42[10] = uVar45;
      *(int *)(puVar42 + 0xb) = (int)uVar19;
      return 2;
    }
    lVar28 = FUN_ffff00009076c008(0,0,0);
    puVar42[4] = lVar28;
    param_1[0xc] = lVar28;
    *(undefined4 *)(puVar42 + 1) = 0x3f3f;
  case 0x3f3f:
    uVar20 = (uint)uVar41;
    if (param_2 - 5U < 2) goto LAB_ffff000090777f64;
code_r0xffff000090776f4c:
    uVar18 = (uint)uVar19;
    if (*(int *)((long)puVar42 + 0xc) == 0) {
      if (uVar18 < 3) {
        if ((int)uVar41 == 0) {
          uVar20 = 0;
          iVar22 = local_7c;
code_r0xffff000090778020:
          iVar31 = iVar22;
          goto code_r0xffff000090778068;
        }
        uVar41 = (ulong)((int)uVar41 - 1);
        uVar18 = uVar18 + 8;
        uVar45 = ((ulong)*pbVar17 << (uVar19 & 0x3f)) + uVar45;
        pbVar17 = pbVar17 + 1;
      }
      uVar20 = (uint)uVar41;
      uVar44 = (uint)uVar45;
      uVar21 = 0x3f41;
      uVar45 = uVar45 >> 3;
      *(uint *)((long)puVar42 + 0xc) = uVar44 & 1;
      switch(uVar44 >> 1 & 3) {
      case 1:
        puVar42[0xd] = &DAT_ffff000090810912;
        puVar42[0xe] = &DAT_ffff000090811112;
        puVar42[0xf] = 0x500000009;
        *(undefined4 *)(puVar42 + 1) = 0x3f47;
        if (param_2 != 6) goto LAB_ffff000090777024;
        uVar18 = uVar18 - 3;
        iVar31 = local_7c;
        goto code_r0xffff000090778068;
      case 2:
        uVar21 = 0x3f44;
        break;
      case 3:
        uVar21 = 0x3f51;
        param_1[6] = (long)s_invalid_block_type_ffff0000907f03d9;
      }
      *(undefined4 *)(puVar42 + 1) = uVar21;
LAB_ffff000090777024:
      iVar14 = *(int *)(puVar42 + 1);
      uVar19 = (ulong)(uVar18 - 3);
    }
    else {
      uVar19 = (ulong)(uVar18 & 0xfffffff8);
      uVar45 = uVar45 >> (uVar18 & 7);
      *(undefined4 *)(puVar42 + 1) = 0x3f4e;
      iVar14 = 0x3f4e;
    }
    goto LAB_ffff000090776498;
  case 0x3f40:
    goto code_r0xffff000090776f4c;
  case 0x3f41:
    uVar44 = uVar18 & 0xfffffff8;
    uVar16 = (ulong)uVar44;
    uVar45 = uVar45 >> (uVar18 & 7);
    if (uVar44 < 0x20) {
      uVar18 = uVar44;
      if (uVar20 == 0) goto LAB_ffff00009077805c;
      uVar43 = uVar20 - 1;
      uVar25 = uVar16 + 8;
      uVar45 = ((ulong)*pbVar17 << (uVar16 & 0x3f)) + uVar45;
      pbVar27 = pbVar17 + 1;
      if (uVar44 < 0x18) {
        pbVar36 = pbVar17 + 1;
        uVar18 = (uint)uVar25;
        if (uVar43 == 0) goto LAB_ffff00009077805c;
        uVar43 = uVar20 - 2;
        uVar41 = uVar25 & 0x3f;
        uVar25 = uVar16 + 0x10;
        uVar45 = ((ulong)pbVar17[1] << uVar41) + uVar45;
        pbVar27 = pbVar17 + 2;
        if (uVar44 < 0x10) {
          pbVar36 = pbVar17 + 2;
          uVar18 = (uint)uVar25;
          if (uVar43 == 0) goto LAB_ffff00009077805c;
          uVar43 = uVar20 - 3;
          uVar41 = uVar25 & 0x3f;
          uVar25 = uVar16 + 0x18;
          uVar45 = ((ulong)pbVar17[2] << uVar41) + uVar45;
          pbVar27 = pbVar17 + 3;
          if ((uVar19 & 0xfffffff8) == 0) {
            pbVar36 = pbVar17 + 3;
            uVar18 = (uint)uVar25;
            if (uVar43 == 0) goto LAB_ffff00009077805c;
            uVar43 = uVar20 - 4;
            uVar19 = uVar25 & 0x3f;
            uVar25 = uVar16 + 0x20;
            uVar45 = ((ulong)pbVar17[3] << uVar19) + uVar45;
            pbVar27 = pbVar17 + 4;
          }
        }
      }
      pbVar17 = pbVar27;
      uVar41 = (ulong)uVar43;
      uVar19 = uVar25 & 0xffffffff;
    }
    else {
      uVar19 = (ulong)uVar44;
    }
    uVar20 = (uint)uVar41;
    uVar18 = (uint)uVar45;
    if ((uVar45 >> 0x10 ^ 0xffff) != (uVar45 & 0xffff)) {
      pcVar26 = s_invalid_stored_block_lengths_ffff0000907deb22;
      pbVar27 = pbVar17;
      goto LAB_ffff000090777edc;
    }
    uVar19 = 0;
    uVar45 = 0;
    *(uint *)((long)puVar42 + 0x5c) = uVar18 & 0xffff;
    *(undefined4 *)(puVar42 + 1) = 0x3f42;
    if (param_2 != 6) goto code_r0xffff000090776b18;
    goto LAB_ffff000090777f64;
  case 0x3f42:
code_r0xffff000090776b18:
    *(undefined4 *)(puVar42 + 1) = 0x3f43;
  case 0x3f43:
    uVar20 = (uint)uVar41;
    uVar18 = *(uint *)((long)puVar42 + 0x5c);
    if (uVar18 == 0) {
      uVar21 = 0x3f3f;
      goto LAB_ffff000090777ee4;
    }
    if (uVar20 <= uVar18) {
      uVar18 = uVar20;
    }
    if (uVar39 <= uVar18) {
      uVar18 = uVar39;
    }
    uVar16 = (ulong)uVar18;
    if (uVar18 == 0) goto LAB_ffff000090777f64;
    FUN_ffff00009077e858(puVar37,pbVar17,uVar16);
    *(uint *)((long)puVar42 + 0x5c) = *(int *)((long)puVar42 + 0x5c) - uVar18;
    iVar14 = *(int *)(puVar42 + 1);
    uVar41 = (ulong)(uVar20 - uVar18);
    pbVar17 = pbVar17 + uVar16;
    uVar39 = uVar39 - uVar18;
    puVar37 = puVar37 + uVar16;
    goto LAB_ffff000090776498;
  case 0x3f44:
    if (uVar18 < 0xe) {
      if (uVar20 == 0) break;
      uVar43 = uVar20 - 1;
      uVar45 = ((ulong)*pbVar17 << (uVar19 & 0x3f)) + uVar45;
      uVar44 = (uint)(uVar19 + 8);
      pbVar27 = pbVar17 + 1;
      if (uVar18 < 6) {
        if (uVar43 == 0) break;
        uVar43 = uVar20 - 2;
        uVar45 = ((ulong)pbVar17[1] << (uVar19 + 8 & 0x3f)) + uVar45;
        pbVar27 = pbVar17 + 2;
        uVar44 = uVar18 + 0x10;
      }
      uVar18 = uVar44;
      pbVar17 = pbVar27;
      uVar41 = (ulong)uVar43;
    }
    uVar43 = (uint)uVar45;
    uVar20 = uVar43 >> 5 & 0x1f;
    uVar45 = uVar45 >> 0xe;
    uVar44 = (uVar43 >> 10 & 0xf) + 4;
    uVar19 = (ulong)(uVar18 - 0xe);
    *(uint *)((long)puVar42 + 0x84) = (uVar43 & 0x1f) + 0x101;
    *(uint *)(puVar42 + 0x11) = uVar20 + 1;
    *(uint *)(puVar42 + 0x10) = uVar44;
    if ((0x1d < (uVar43 & 0x1f)) || (0x1d < uVar20)) {
      pcVar26 = s_too_many_length_or_distance_symb_ffff0000907d3755;
      pbVar27 = pbVar17;
      goto LAB_ffff000090777edc;
    }
    uVar18 = 0;
    *(undefined4 *)((long)puVar42 + 0x8c) = 0;
    *(undefined4 *)(puVar42 + 1) = 0x3f45;
LAB_ffff000090776844:
    pbVar36 = pbVar17;
    uVar16 = (ulong)uVar18;
    do {
      uVar18 = (uint)uVar19;
      pbVar17 = pbVar36;
      if (uVar18 < 3) {
        if ((int)uVar41 == 0) goto LAB_ffff00009077805c;
        pbVar17 = pbVar36 + 1;
        uVar41 = (ulong)((int)uVar41 - 1);
        uVar18 = uVar18 + 8;
        uVar45 = ((ulong)*pbVar36 << (uVar19 & 0x3f)) + uVar45;
      }
      uVar25 = uVar16 + 1;
      uVar12 = (ushort)uVar45;
      uVar10 = *(ushort *)(&DAT_ffff0000908108ec + uVar16 * 2);
      uVar45 = uVar45 >> 3;
      uVar19 = (ulong)(uVar18 - 3);
      *(int *)((long)puVar42 + 0x8c) = (int)uVar25;
      *(ushort *)((long)puVar42 + (ulong)uVar10 * 2 + 0x98) = uVar12 & 7;
      pbVar36 = pbVar17;
      uVar16 = uVar25;
      uVar18 = uVar44;
    } while (uVar44 != uVar25);
    goto LAB_ffff000090776a38;
  case 0x3f45:
    uVar44 = *(uint *)(puVar42 + 0x10);
    uVar18 = *(uint *)((long)puVar42 + 0x8c);
    if (uVar18 < uVar44) goto LAB_ffff000090776844;
LAB_ffff000090776a38:
    if (uVar18 < 0x13) {
      uVar16 = (ulong)uVar18;
      uVar25 = 0x14 - (ulong)(uVar18 + 1);
      if (((uVar25 < 2) || (uVar35 = 0x13 - (ulong)(uVar18 + 1), -uVar18 - 2 < (uint)uVar35)) ||
         (uVar35 >> 0x20 != 0)) {
LAB_ffff000090776a6c:
        do {
          uVar25 = uVar16 + 1;
          *(undefined2 *)
           ((long)puVar42 + (ulong)*(ushort *)(&DAT_ffff0000908108ec + uVar16 * 2) * 2 + 0x98) = 0;
          uVar16 = uVar25;
        } while ((int)uVar25 != 0x13);
      }
      else {
        uVar30 = uVar25 & 0xfffffffffffffffe;
        puVar32 = (ushort *)(&DAT_ffff0000908108ee + uVar16 * 2);
        uVar35 = uVar30;
        do {
          uVar35 = uVar35 - 2;
          uVar10 = *puVar32;
          *(undefined2 *)((long)puVar42 + (ulong)puVar32[-1] * 2 + 0x98) = 0;
          *(undefined2 *)((long)puVar42 + (ulong)uVar10 * 2 + 0x98) = 0;
          puVar32 = puVar32 + 2;
        } while (uVar35 != 0);
        uVar16 = uVar30 + uVar16;
        if (uVar25 != uVar30) goto LAB_ffff000090776a6c;
      }
      *(undefined4 *)((long)puVar42 + 0x8c) = 0x13;
    }
    *(undefined4 *)(puVar42 + 0xf) = 7;
    puVar42[0x12] = puVar2;
    puVar42[0xd] = puVar2;
    local_7c = FUN_ffff000090778388(0,puVar5,0x13,puVar3,puVar42 + 0xf,puVar4);
    if (local_7c != 0) {
      iVar14 = 0x3f51;
      param_1[6] = (long)s_invalid_code_lengths_set_ffff0000907dd240;
      goto LAB_ffff00009077778c;
    }
    uVar44 = 0;
    local_7c = 0;
    *(undefined4 *)((long)puVar42 + 0x8c) = 0;
    *(undefined4 *)(puVar42 + 1) = 0x3f46;
    pbVar27 = pbVar17;
LAB_ffff000090776c04:
    iVar14 = *(int *)((long)puVar42 + 0x84);
    uVar43 = *(int *)(puVar42 + 0x11) + iVar14;
    if (uVar44 < uVar43) {
      lVar28 = puVar42[0xd];
      uVar29 = ~(-1 << (ulong)(*(uint *)(puVar42 + 0xf) & 0x1f));
      pbVar17 = pbVar27;
      uVar16 = uVar41;
      do {
        uVar35 = (ulong)(uVar29 & (uint)uVar45);
        bVar8 = *(byte *)(lVar28 + uVar35 * 4 + 1);
        uVar25 = (ulong)bVar8;
        uVar18 = (uint)uVar19;
        pbVar27 = pbVar17;
        uVar41 = uVar16;
        if (uVar18 < bVar8) {
          uVar41 = uVar16 & 0xffffffff;
          pbVar36 = pbVar17;
          do {
            if ((int)uVar41 == 0) goto LAB_ffff000090777f48;
            pbVar27 = pbVar36 + 1;
            uVar41 = (ulong)((int)uVar41 - 1);
            uVar25 = uVar19 & 0x3f;
            uVar19 = uVar19 + 8;
            uVar45 = ((ulong)*pbVar36 << uVar25) + uVar45;
            uVar35 = (ulong)(uVar29 & (uint)uVar45);
            uVar25 = (ulong)*(byte *)(lVar28 + uVar35 * 4 + 1);
            pbVar36 = pbVar27;
          } while (uVar19 < uVar25);
          uVar19 = uVar19 & 0xffffffff;
        }
        uVar10 = *(ushort *)(lVar28 + uVar35 * 4 + 2);
        iVar31 = (int)uVar25;
        uVar18 = (uint)uVar19;
        if (0xf < uVar10) {
          if (uVar10 == 0x10) {
            pbVar17 = pbVar27;
            if (uVar18 < iVar31 + 2U) {
              do {
                uVar20 = 0;
                if ((int)uVar41 == 0) goto LAB_ffff000090777f64;
                pbVar27 = pbVar17 + 1;
                uVar41 = (ulong)((int)uVar41 - 1);
                uVar16 = uVar19 & 0x3f;
                uVar19 = uVar19 + 8;
                uVar45 = ((ulong)*pbVar17 << uVar16) + uVar45;
                pbVar17 = pbVar27;
              } while (uVar19 < iVar31 + 2U);
            }
            uVar45 = uVar45 >> (uVar25 & 0x3f);
            uVar18 = (int)uVar19 - iVar31;
            uVar19 = (ulong)uVar18;
            if (uVar44 != 0) {
              uVar19 = (ulong)(uVar18 - 2);
              uVar20 = ((uint)uVar45 & 3) + 3;
              uVar45 = uVar45 >> 2;
              uVar33 = *(undefined2 *)((long)puVar42 + (ulong)(uVar44 - 1) * 2 + 0x98);
              goto LAB_ffff000090776dc0;
            }
          }
          else {
            if (uVar10 == 0x11) {
              pbVar17 = pbVar27;
              if (uVar18 < iVar31 + 3U) {
                do {
                  if ((int)uVar41 == 0) goto LAB_ffff000090777f60;
                  pbVar17 = pbVar27 + 1;
                  uVar41 = (ulong)((int)uVar41 - 1);
                  uVar16 = uVar19 & 0x3f;
                  uVar19 = uVar19 + 8;
                  uVar45 = ((ulong)*pbVar27 << uVar16) + uVar45;
                  pbVar27 = pbVar17;
                } while (uVar19 < iVar31 + 3U);
                uVar18 = (uint)uVar19;
              }
              uVar45 = uVar45 >> (uVar25 & 0x3f);
              uVar33 = 0;
              uVar20 = ((uint)uVar45 & 7) + 3;
              uVar45 = uVar45 >> 3;
              uVar19 = (ulong)((uVar18 - iVar31) - 3);
              pbVar27 = pbVar17;
            }
            else {
              pbVar17 = pbVar27;
              if (uVar18 < iVar31 + 7U) {
                do {
                  if ((int)uVar41 == 0) goto LAB_ffff000090777f60;
                  pbVar17 = pbVar27 + 1;
                  uVar41 = (ulong)((int)uVar41 - 1);
                  uVar16 = uVar19 & 0x3f;
                  uVar19 = uVar19 + 8;
                  uVar45 = ((ulong)*pbVar27 << uVar16) + uVar45;
                  pbVar27 = pbVar17;
                } while (uVar19 < iVar31 + 7U);
                uVar18 = (uint)uVar19;
              }
              uVar45 = uVar45 >> (uVar25 & 0x3f);
              uVar33 = 0;
              uVar20 = ((uint)uVar45 & 0x7f) + 0xb;
              uVar45 = uVar45 >> 7;
              uVar19 = (ulong)((uVar18 - iVar31) - 7);
              pbVar27 = pbVar17;
            }
LAB_ffff000090776dc0:
            if (uVar20 + uVar44 <= uVar43) {
              if (CARRY4(uVar44,uVar20 - 1)) {
LAB_ffff000090776e1c:
                do {
                  uVar18 = uVar44 + 1;
                  uVar20 = uVar20 - 1;
                  *(undefined2 *)((long)puVar42 + (ulong)uVar44 * 2 + 0x98) = uVar33;
                  uVar44 = uVar18;
                } while (uVar20 != 0);
              }
              else {
                uVar6 = uVar20 & 0xfe;
                uVar18 = uVar44 + uVar6;
                uVar11 = uVar6;
                do {
                  uVar1 = uVar44 + 1;
                  uVar11 = uVar11 - 2;
                  *(undefined2 *)((long)puVar42 + (ulong)uVar44 * 2 + 0x98) = uVar33;
                  uVar44 = uVar44 + 2;
                  *(undefined2 *)((long)puVar42 + (ulong)uVar1 * 2 + 0x98) = uVar33;
                } while (uVar11 != 0);
                bVar13 = uVar20 != uVar6;
                uVar20 = uVar20 & 1;
                uVar44 = uVar18;
                if (bVar13) goto LAB_ffff000090776e1c;
              }
              *(uint *)((long)puVar42 + 0x8c) = uVar44;
              goto LAB_ffff000090776e3c;
            }
          }
          pcVar26 = s_invalid_bit_length_repeat_ffff0000907d9cce;
          goto LAB_ffff000090777edc;
        }
        uVar45 = uVar45 >> (uVar25 & 0x3f);
        uVar19 = (ulong)(uVar18 - iVar31);
        *(uint *)((long)puVar42 + 0x8c) = uVar44 + 1;
        *(ushort *)((long)puVar42 + (ulong)uVar44 * 2 + 0x98) = uVar10;
        uVar44 = uVar44 + 1;
LAB_ffff000090776e3c:
        pbVar17 = pbVar27;
        uVar16 = uVar41;
      } while (uVar44 < uVar43);
    }
    uVar20 = (uint)uVar41;
    if (*(short *)(puVar42 + 0x53) == 0) {
      pcVar26 = s_invalid_code____missing_end_of_b_ffff0000907deb3f;
      goto LAB_ffff000090777edc;
    }
    *(undefined4 *)(puVar42 + 0xf) = 9;
    puVar42[0x12] = puVar2;
    puVar42[0xd] = puVar2;
    local_7c = FUN_ffff000090778388(1,puVar5,iVar14,puVar3,puVar42 + 0xf,puVar4);
    pbVar17 = pbVar27;
    if (local_7c == 0) {
      puVar42[0xe] = puVar42[0x12];
      *(undefined4 *)((long)puVar42 + 0x7c) = 6;
      local_7c = FUN_ffff000090778388
                           (2,(long)puVar5 + (ulong)*(uint *)((long)puVar42 + 0x84) * 2,
                            *(undefined4 *)(puVar42 + 0x11),puVar3,(long)puVar42 + 0x7c,puVar4);
      if (local_7c != 0) {
        *(undefined4 *)(puVar42 + 1) = 0x3f51;
        param_1[6] = (long)s_invalid_distances_set_ffff0000907fa672;
        goto LAB_ffff000090777810;
      }
      local_7c = 0;
      *(undefined4 *)(puVar42 + 1) = 0x3f47;
      if (param_2 == 6) goto LAB_ffff000090777f64;
code_r0xffff0000907779b0:
      *(undefined4 *)(puVar42 + 1) = 0x3f48;
      uVar16 = uVar41;
code_r0xffff0000907779bc:
      uVar18 = (uint)uVar19;
      if (((uint)uVar16 < 6) || (uVar39 < 0x102)) {
        lVar28 = puVar42[0xd];
        *(undefined4 *)((long)puVar42 + 0x1bec) = 0;
        uVar44 = -1 << (ulong)(*(uint *)(puVar42 + 0xf) & 0x1f);
        pbVar36 = (byte *)(lVar28 + (ulong)((uint)uVar45 & (uVar44 ^ 0xffffffff)) * 4);
        uVar25 = (ulong)pbVar36[1];
        pbVar17 = pbVar27;
        uVar41 = uVar16;
        if (uVar18 < pbVar36[1]) {
          uVar41 = uVar16 & 0xffffffff;
          pbVar34 = pbVar27;
          do {
            pbVar17 = pbVar27;
            if ((int)uVar41 == 0) goto LAB_ffff000090777f48;
            pbVar17 = pbVar34 + 1;
            uVar41 = (ulong)((int)uVar41 - 1);
            uVar25 = uVar19 & 0x3f;
            uVar19 = uVar19 + 8;
            uVar45 = ((ulong)*pbVar34 << uVar25) + uVar45;
            pbVar36 = (byte *)(lVar28 + (ulong)(~uVar44 & (uint)uVar45) * 4);
            uVar25 = (ulong)pbVar36[1];
            pbVar34 = pbVar17;
          } while (uVar19 < uVar25);
          uVar19 = uVar19 & 0xffffffff;
        }
        uVar18 = (uint)uVar19;
        bVar8 = *pbVar36;
        uVar44 = (uint)uVar25;
        uVar10 = *(ushort *)(pbVar36 + 2);
        if (bVar8 - 1 < 0xf) {
          uVar20 = -1 << (ulong)(uVar44 + bVar8 & 0x1f);
          pbVar27 = (byte *)(lVar28 + (ulong)((((uint)uVar45 & (uVar20 ^ 0xffffffff)) >>
                                              (ulong)(uVar44 & 0x1f)) + (uint)uVar10) * 4);
          bVar9 = pbVar27[1];
          pbVar36 = pbVar17;
          uVar35 = uVar41;
          if (uVar18 < uVar44 + bVar9) {
            uVar35 = uVar41 & 0xffffffff;
            pbVar34 = pbVar17;
            do {
              uVar16 = uVar41;
              if ((int)uVar35 == 0) goto LAB_ffff000090777f48;
              pbVar36 = pbVar34 + 1;
              uVar35 = (ulong)((int)uVar35 - 1);
              uVar16 = uVar19 & 0x3f;
              uVar19 = uVar19 + 8;
              uVar45 = ((ulong)*pbVar34 << uVar16) + uVar45;
              pbVar27 = (byte *)(lVar28 + (ulong)((((uint)uVar45 & ~uVar20) >>
                                                  (ulong)(uVar44 & 0x1f)) + (uint)uVar10) * 4);
              bVar9 = pbVar27[1];
              pbVar34 = pbVar36;
            } while (uVar19 < uVar44 + bVar9);
            uVar18 = (uint)uVar19;
          }
          uVar10 = *(ushort *)(pbVar27 + 2);
          uVar45 = uVar45 >> (uVar25 & 0x3f);
          bVar8 = *pbVar27;
          uVar18 = uVar18 - uVar44;
          pbVar17 = pbVar36;
          uVar25 = (ulong)bVar9;
          uVar41 = uVar35;
        }
        else {
          uVar44 = 0;
        }
        uVar45 = uVar45 >> (uVar25 & 0x3f);
        uVar19 = (ulong)(uVar18 - (int)uVar25);
        *(uint *)((long)puVar42 + 0x5c) = (uint)uVar10;
        *(uint *)((long)puVar42 + 0x1bec) = uVar44 + (int)uVar25;
        if (bVar8 == 0) {
          uVar21 = 0x3f4d;
          goto LAB_ffff000090777ee4;
        }
        if ((bVar8 >> 5 & 1) == 0) {
          if ((bVar8 >> 6 & 1) != 0) {
            pcVar26 = s_invalid_literal_length_code_ffff0000907ffb65;
            pbVar27 = pbVar17;
            goto LAB_ffff000090777edc;
          }
          uVar44 = bVar8 & 0xf;
          *(uint *)((long)puVar42 + 100) = uVar44;
          *(undefined4 *)(puVar42 + 1) = 0x3f49;
          if ((bVar8 & 0xf) == 0) {
LAB_ffff000090777b90:
            iVar14 = *(int *)((long)puVar42 + 0x5c);
          }
          else {
LAB_ffff0000907768c4:
            uVar18 = (uint)uVar19;
            if (uVar18 < uVar44) {
              uVar25 = uVar41 & 0xffffffff;
              pbVar36 = pbVar17;
              do {
                uVar16 = uVar41;
                if ((int)uVar25 == 0) goto LAB_ffff000090777f48;
                pbVar27 = pbVar36 + 1;
                uVar25 = (ulong)((int)uVar25 - 1);
                uVar16 = uVar19 & 0x3f;
                uVar20 = (int)uVar19 + 8;
                uVar19 = (ulong)uVar20;
                uVar45 = ((ulong)*pbVar36 << uVar16) + uVar45;
                pbVar36 = pbVar27;
              } while (uVar20 < uVar44);
            }
            else {
              uVar25 = uVar41 & 0xffffffff;
              pbVar27 = pbVar17;
              uVar20 = uVar18;
            }
            pbVar17 = pbVar27;
            uVar19 = (ulong)(uVar20 - uVar44);
            iVar14 = *(int *)((long)puVar42 + 0x5c) +
                     ((uint)uVar45 & (-1 << (ulong)(uVar44 & 0x1f) ^ 0xffffffffU));
            uVar45 = uVar45 >> ((ulong)uVar44 & 0x3f);
            *(int *)((long)puVar42 + 0x5c) = iVar14;
            *(uint *)((long)puVar42 + 0x1bec) = *(int *)((long)puVar42 + 0x1bec) + uVar44;
            uVar41 = uVar25;
          }
          *(int *)(puVar42 + 0x37e) = iVar14;
          *(undefined4 *)(puVar42 + 1) = 0x3f4a;
          pbVar27 = pbVar17;
          uVar16 = uVar41;
code_r0xffff000090777ba4:
          lVar28 = puVar42[0xe];
          uVar44 = -1 << (ulong)(*(uint *)((long)puVar42 + 0x7c) & 0x1f);
          pbVar36 = (byte *)(lVar28 + (ulong)((uint)uVar45 & (uVar44 ^ 0xffffffff)) * 4);
          uVar25 = (ulong)pbVar36[1];
          uVar18 = (uint)uVar19;
          pbVar17 = pbVar27;
          uVar41 = uVar16;
          if (uVar18 < pbVar36[1]) {
            uVar41 = uVar16 & 0xffffffff;
            pbVar34 = pbVar27;
            do {
              pbVar17 = pbVar27;
              if ((int)uVar41 == 0) goto LAB_ffff000090777f48;
              pbVar17 = pbVar34 + 1;
              uVar41 = (ulong)((int)uVar41 - 1);
              uVar25 = uVar19 & 0x3f;
              uVar19 = uVar19 + 8;
              uVar45 = ((ulong)*pbVar34 << uVar25) + uVar45;
              pbVar36 = (byte *)(lVar28 + (ulong)(~uVar44 & (uint)uVar45) * 4);
              uVar25 = (ulong)pbVar36[1];
              pbVar34 = pbVar17;
            } while (uVar19 < uVar25);
            uVar19 = uVar19 & 0xffffffff;
          }
          uVar18 = (uint)uVar19;
          bVar8 = *pbVar36;
          uVar10 = *(ushort *)(pbVar36 + 2);
          if (bVar8 < 0x10) {
            uVar20 = (uint)uVar25;
            uVar44 = -1 << (ulong)(uVar20 + bVar8 & 0x1f);
            pbVar27 = (byte *)(lVar28 + (ulong)((((uint)uVar45 & (uVar44 ^ 0xffffffff)) >>
                                                (ulong)(uVar20 & 0x1f)) + (uint)uVar10) * 4);
            bVar9 = pbVar27[1];
            pbVar36 = pbVar17;
            uVar35 = uVar41;
            if (uVar18 < uVar20 + bVar9) {
              uVar35 = uVar41 & 0xffffffff;
              pbVar34 = pbVar17;
              do {
                uVar16 = uVar41;
                if ((int)uVar35 == 0) goto LAB_ffff000090777f48;
                pbVar36 = pbVar34 + 1;
                uVar35 = (ulong)((int)uVar35 - 1);
                uVar16 = uVar19 & 0x3f;
                uVar19 = uVar19 + 8;
                uVar45 = ((ulong)*pbVar34 << uVar16) + uVar45;
                pbVar27 = (byte *)(lVar28 + (ulong)((((uint)uVar45 & ~uVar44) >>
                                                    (ulong)(uVar20 & 0x1f)) + (uint)uVar10) * 4);
                bVar9 = pbVar27[1];
                pbVar34 = pbVar36;
              } while (uVar19 < uVar20 + bVar9);
              uVar18 = (uint)uVar19;
            }
            uVar45 = uVar45 >> (uVar25 & 0x3f);
            uVar10 = *(ushort *)(pbVar27 + 2);
            uVar18 = uVar18 - uVar20;
            bVar8 = *pbVar27;
            iVar14 = *(int *)((long)puVar42 + 0x1bec) + uVar20;
            pbVar17 = pbVar36;
            uVar25 = (ulong)bVar9;
            uVar41 = uVar35;
          }
          else {
            iVar14 = *(int *)((long)puVar42 + 0x1bec);
          }
          uVar45 = uVar45 >> (uVar25 & 0x3f);
          uVar19 = (ulong)(uVar18 - (int)uVar25);
          *(int *)((long)puVar42 + 0x1bec) = iVar14 + (int)uVar25;
          if ((bVar8 >> 6 & 1) != 0) {
            pcVar26 = s_invalid_distance_code_ffff0000907fa65c;
            pbVar27 = pbVar17;
            goto LAB_ffff000090777edc;
          }
          uVar44 = bVar8 & 0xf;
          *(uint *)(puVar42 + 0xc) = (uint)uVar10;
          *(uint *)((long)puVar42 + 100) = uVar44;
          *(undefined4 *)(puVar42 + 1) = 0x3f4b;
LAB_ffff000090777ce8:
          uVar18 = (uint)uVar19;
          uVar25 = uVar41;
          if (uVar44 != 0) {
            if (uVar18 < uVar44) {
              uVar25 = uVar41 & 0xffffffff;
              pbVar36 = pbVar17;
              do {
                uVar16 = uVar41;
                if ((int)uVar25 == 0) goto LAB_ffff000090777f48;
                pbVar27 = pbVar36 + 1;
                uVar25 = (ulong)((int)uVar25 - 1);
                uVar16 = uVar19 & 0x3f;
                uVar20 = (int)uVar19 + 8;
                uVar19 = (ulong)uVar20;
                uVar45 = ((ulong)*pbVar36 << uVar16) + uVar45;
                pbVar36 = pbVar27;
              } while (uVar20 < uVar44);
            }
            else {
              uVar25 = uVar41 & 0xffffffff;
              pbVar27 = pbVar17;
              uVar20 = uVar18;
            }
            pbVar17 = pbVar27;
            uVar18 = (uint)uVar45;
            uVar19 = (ulong)(uVar20 - uVar44);
            uVar45 = uVar45 >> ((ulong)uVar44 & 0x3f);
            *(uint *)(puVar42 + 0xc) =
                 *(int *)(puVar42 + 0xc) + (uVar18 & (-1 << (ulong)(uVar44 & 0x1f) ^ 0xffffffffU));
            *(uint *)((long)puVar42 + 0x1bec) = *(int *)((long)puVar42 + 0x1bec) + uVar44;
          }
          *(undefined4 *)(puVar42 + 1) = 0x3f4c;
          uVar41 = uVar25;
code_r0xffff000090777d7c:
          uVar20 = (uint)uVar41;
          if (uVar39 == 0) goto LAB_ffff000090777f64;
          uVar18 = *(uint *)(puVar42 + 0xc);
          if (uVar40 - uVar39 < uVar18) {
            uVar18 = uVar18 - (uVar40 - uVar39);
            if ((*(uint *)(puVar42 + 8) < uVar18) && (*(int *)(puVar42 + 0x37d) != 0)) {
              pcVar26 = s_invalid_distance_too_far_back_ffff0000907eb686;
              pbVar27 = pbVar17;
              goto LAB_ffff000090777edc;
            }
            uVar20 = *(uint *)((long)puVar42 + 0x44);
            uVar44 = uVar18 - uVar20;
            if (uVar18 < uVar20 || uVar44 == 0) {
              uVar20 = uVar20 - uVar18;
            }
            else {
              uVar20 = *(int *)((long)puVar42 + 0x3c) - uVar44;
              uVar18 = uVar44;
            }
            uVar44 = *(uint *)((long)puVar42 + 0x5c);
            puVar24 = (undefined1 *)(puVar42[9] + (ulong)uVar20);
            if (uVar44 <= uVar18) {
              uVar18 = uVar44;
            }
          }
          else {
            uVar44 = *(uint *)((long)puVar42 + 0x5c);
            puVar24 = puVar37 + -(ulong)uVar18;
            uVar18 = uVar44;
          }
          if (uVar39 <= uVar18) {
            uVar18 = uVar39;
          }
          *(uint *)((long)puVar42 + 0x5c) = uVar44 - uVar18;
          puVar38 = puVar37;
          uVar44 = uVar18;
          do {
            uVar44 = uVar44 - 1;
            puVar37 = puVar38 + 1;
            *puVar38 = *puVar24;
            puVar24 = puVar24 + 1;
            puVar38 = puVar37;
          } while (uVar44 != 0);
          uVar39 = uVar39 - uVar18;
          if (*(int *)((long)puVar42 + 0x5c) == 0) {
            uVar21 = 0x3f48;
            goto LAB_ffff000090777ee4;
          }
          goto LAB_ffff000090777ee8;
        }
        *(undefined4 *)((long)puVar42 + 0x1bec) = 0xffffffff;
        iVar14 = 0x3f3f;
        *(undefined4 *)(puVar42 + 1) = 0x3f3f;
      }
      else {
        param_1[3] = (long)puVar37;
        *(uint *)(param_1 + 4) = uVar39;
        *param_1 = (long)pbVar27;
        *(uint *)(param_1 + 1) = (uint)uVar16;
        puVar42[10] = uVar45;
        *(uint *)(puVar42 + 0xb) = uVar18;
        FUN_ffff000090775ba8(param_1,uVar40);
        puVar37 = (undefined1 *)param_1[3];
        uVar39 = *(uint *)(param_1 + 4);
        pbVar27 = (byte *)*param_1;
        uVar41 = (ulong)*(uint *)(param_1 + 1);
        uVar45 = puVar42[10];
        uVar19 = (ulong)*(uint *)(puVar42 + 0xb);
        if (*(int *)(puVar42 + 1) == 0x3f3f) {
          *(undefined4 *)((long)puVar42 + 0x1bec) = 0xffffffff;
        }
LAB_ffff000090777a20:
        iVar14 = *(int *)(puVar42 + 1);
        pbVar17 = pbVar27;
      }
    }
    else {
      param_1[6] = (long)s_invalid_literal_lengths_set_ffff0000907d1dbf;
      *(undefined4 *)(puVar42 + 1) = 0x3f51;
LAB_ffff000090777810:
      iVar14 = *(int *)(puVar42 + 1);
    }
    goto LAB_ffff000090776498;
  case 0x3f46:
    uVar44 = *(uint *)((long)puVar42 + 0x8c);
    goto LAB_ffff000090776c04;
  case 0x3f47:
    goto code_r0xffff0000907779b0;
  case 0x3f48:
    goto code_r0xffff0000907779bc;
  case 0x3f49:
    uVar44 = *(uint *)((long)puVar42 + 100);
    if (uVar44 != 0) goto LAB_ffff0000907768c4;
    goto LAB_ffff000090777b90;
  case 0x3f4a:
    goto code_r0xffff000090777ba4;
  case 0x3f4b:
    uVar44 = *(uint *)((long)puVar42 + 100);
    goto LAB_ffff000090777ce8;
  case 0x3f4c:
    goto code_r0xffff000090777d7c;
  case 0x3f4d:
    if (uVar39 == 0) goto LAB_ffff000090777f64;
    uVar39 = uVar39 - 1;
    *puVar37 = (char)*(undefined4 *)((long)puVar42 + 0x5c);
    *(undefined4 *)(puVar42 + 1) = 0x3f48;
    iVar14 = 0x3f48;
    puVar37 = puVar37 + 1;
    goto LAB_ffff000090776498;
  case 0x3f4e:
    uVar43 = *(uint *)(puVar42 + 2);
    if (uVar43 == 0) {
LAB_ffff000090777880:
      *(undefined4 *)(puVar42 + 1) = 0x3f4f;
      goto LAB_ffff000090777888;
    }
    if (uVar18 < 0x20) {
      if (uVar20 == 0) goto LAB_ffff000090778038;
      uVar29 = uVar20 - 1;
      uVar45 = ((ulong)*pbVar17 << (uVar19 & 0x3f)) + uVar45;
      uVar16 = uVar19 + 8;
      pbVar27 = pbVar17 + 1;
      if (uVar18 < 0x18) {
        pbVar36 = pbVar17 + 1;
        uVar44 = (uint)uVar16;
        if (uVar29 == 0) goto LAB_ffff000090778038;
        uVar29 = uVar20 - 2;
        uVar45 = ((ulong)pbVar17[1] << (uVar16 & 0x3f)) + uVar45;
        uVar16 = uVar19 + 0x10;
        pbVar27 = pbVar17 + 2;
        if (uVar18 < 0x10) {
          pbVar36 = pbVar17 + 2;
          uVar44 = (uint)uVar16;
          if (uVar29 == 0) goto LAB_ffff000090778038;
          uVar29 = uVar20 - 3;
          uVar45 = ((ulong)pbVar17[2] << (uVar16 & 0x3f)) + uVar45;
          uVar16 = uVar19 + 0x18;
          pbVar27 = pbVar17 + 3;
          if (uVar18 < 8) {
            pbVar36 = pbVar17 + 3;
            uVar44 = (uint)uVar16;
            if (uVar29 == 0) goto LAB_ffff000090778038;
            uVar29 = uVar20 - 4;
            uVar45 = ((ulong)pbVar17[3] << (uVar16 & 0x3f)) + uVar45;
            uVar16 = uVar19 + 0x20;
            pbVar27 = pbVar17 + 4;
          }
        }
      }
      pbVar17 = pbVar27;
      uVar41 = (ulong)uVar29;
      uVar19 = uVar16 & 0xffffffff;
    }
    uVar16 = (ulong)(uVar40 - uVar39);
    lVar28 = puVar42[5];
    uVar18 = uVar43 & 4;
    param_1[5] = param_1[5] + uVar16;
    puVar42[5] = lVar28 + uVar16;
    if (((uVar43 >> 2 & 1) != 0) && (uVar40 != uVar39)) {
      if (*(int *)(puVar42 + 3) == 0) {
        lVar28 = FUN_ffff00009076c008(puVar42[4],(long)puVar37 - uVar16);
      }
      else {
        lVar28 = FUN_ffff00009076c7c4();
      }
      uVar43 = *(uint *)(puVar42 + 2);
      uVar18 = uVar43 & 4;
      puVar42[4] = lVar28;
      param_1[0xc] = lVar28;
    }
    uVar40 = uVar39;
    if (uVar18 == 0) {
LAB_ffff000090777874:
      uVar45 = 0;
      uVar19 = 0;
      goto LAB_ffff000090777880;
    }
    uVar18 = ((uint)uVar45 & 0xff00ff00) >> 8 | ((uint)uVar45 & 0xff00ff) << 8;
    uVar16 = (ulong)(uVar18 >> 0x10 | uVar18 << 0x10);
    if (*(int *)(puVar42 + 3) != 0) {
      uVar16 = uVar45;
    }
    if (uVar16 == puVar42[4]) goto LAB_ffff000090777874;
    param_1[6] = (long)s_incorrect_data_check_ffff0000907deb64;
    iVar14 = 0x3f51;
    *(undefined4 *)(puVar42 + 1) = 0x3f51;
    goto LAB_ffff000090776498;
  case 0x3f4f:
    uVar43 = *(uint *)(puVar42 + 2);
LAB_ffff000090777888:
    uVar18 = (uint)uVar19;
    uVar20 = (uint)uVar41;
    if ((uVar43 != 0) && (*(int *)(puVar42 + 3) != 0)) {
      if (0x1f < uVar18) {
LAB_ffff000090777934:
        uVar20 = (uint)uVar41;
        if (((uVar43 >> 2 & 1) != 0) && (uVar45 != *(uint *)(puVar42 + 5))) {
          pcVar26 = s_incorrect_length_check_ffff0000908014de;
          pbVar27 = pbVar17;
          goto LAB_ffff000090777edc;
        }
        uVar45 = 0;
        uVar18 = 0;
        goto LAB_ffff000090777f88;
      }
      pbVar36 = pbVar17;
      uVar44 = uVar18;
      if (uVar20 != 0) {
        uVar29 = uVar20 - 1;
        uVar45 = ((ulong)*pbVar17 << (uVar19 & 0x3f)) + uVar45;
        uVar16 = uVar19 + 8;
        pbVar27 = pbVar17 + 1;
        if (uVar18 < 0x18) {
          pbVar36 = pbVar17 + 1;
          uVar44 = (uint)uVar16;
          if (uVar29 == 0) goto LAB_ffff000090778038;
          uVar29 = uVar20 - 2;
          uVar45 = ((ulong)pbVar17[1] << (uVar16 & 0x3f)) + uVar45;
          uVar16 = uVar19 + 0x10;
          pbVar27 = pbVar17 + 2;
          if (uVar18 < 0x10) {
            pbVar36 = pbVar17 + 2;
            uVar44 = (uint)uVar16;
            if (uVar29 == 0) goto LAB_ffff000090778038;
            uVar29 = uVar20 - 3;
            uVar45 = ((ulong)pbVar17[2] << (uVar16 & 0x3f)) + uVar45;
            uVar16 = uVar19 + 0x18;
            pbVar27 = pbVar17 + 3;
            if (uVar18 < 8) {
              pbVar36 = pbVar17 + 3;
              uVar44 = (uint)uVar16;
              if (uVar29 == 0) goto LAB_ffff000090778038;
              uVar29 = uVar20 - 4;
              uVar45 = ((ulong)pbVar17[3] << (uVar16 & 0x3f)) + uVar45;
              uVar16 = uVar19 + 0x20;
              pbVar27 = pbVar17 + 4;
            }
          }
        }
        pbVar17 = pbVar27;
        uVar41 = (ulong)uVar29;
        uVar19 = uVar16 & 0xffffffff;
        goto LAB_ffff000090777934;
      }
      goto LAB_ffff000090778038;
    }
LAB_ffff000090777f88:
    *(undefined4 *)(puVar42 + 1) = 0x3f50;
    iVar31 = 1;
  case 0x3f50:
    goto code_r0xffff000090778068;
  case 0x3f51:
    goto code_r0xffff000090778020;
  case 0x3f52:
    return -4;
  default:
    goto LAB_ffff000090777f24;
  }
  uVar18 = uVar44;
  pbVar17 = pbVar27;
  uVar20 = 0;
  iVar31 = local_7c;
  goto code_r0xffff000090778068;
LAB_ffff000090777f60:
  uVar20 = 0;
  uVar19 = uVar19 & 0xffffffff;
  pbVar17 = pbVar27;
LAB_ffff000090777f64:
  uVar18 = (uint)uVar19;
  iVar31 = local_7c;
  goto code_r0xffff000090778068;
LAB_ffff000090777f48:
  uVar20 = 0;
  pbVar17 = pbVar17 + (uVar16 & 0xffffffff);
  uVar18 = uVar18 + (int)uVar16 * 8;
  iVar31 = local_7c;
code_r0xffff000090778068:
  iVar14 = *(int *)((long)puVar42 + 0x3c);
  param_1[3] = (long)puVar37;
  *(uint *)(param_1 + 4) = uVar39;
  *param_1 = (long)pbVar17;
  *(uint *)(param_1 + 1) = uVar20;
  puVar42[10] = uVar45;
  *(uint *)(puVar42 + 0xb) = uVar18;
  if ((iVar14 != 0) ||
     (((uVar40 != uVar39 && (*(uint *)(puVar42 + 1) < 0x3f51)) &&
      ((param_2 != 4 || (*(uint *)(puVar42 + 1) < 0x3f4e)))))) {
    iVar14 = FUN_ffff0000907781c0(param_1,puVar37,uVar40 - uVar39);
    if (iVar14 != 0) {
      *(undefined4 *)(puVar42 + 1) = 0x3f52;
      return -4;
    }
    uVar20 = *(uint *)(param_1 + 1);
    uVar39 = *(uint *)(param_1 + 4);
  }
  uVar19 = (ulong)(uVar40 - uVar39);
  lVar28 = puVar42[5];
  param_1[2] = param_1[2] + (ulong)(uVar7 - uVar20);
  param_1[5] = param_1[5] + uVar19;
  puVar42[5] = lVar28 + uVar19;
  if (((*(byte *)(puVar42 + 2) >> 2 & 1) != 0) && (uVar40 != uVar39)) {
    if (*(int *)(puVar42 + 3) == 0) {
      lVar28 = FUN_ffff00009076c008(puVar42[4],param_1[3] - uVar19);
    }
    else {
      lVar28 = FUN_ffff00009076c7c4();
    }
    puVar42[4] = lVar28;
    param_1[0xc] = lVar28;
  }
  iVar22 = *(int *)(puVar42 + 1);
  iVar14 = 0x100;
  if (iVar22 != 0x3f42 && iVar22 != 0x3f47) {
    iVar14 = 0;
  }
  *(uint *)(param_1 + 0xb) =
       *(int *)(puVar42 + 0xb) + (uint)(*(int *)((long)puVar42 + 0xc) != 0) * 0x40 +
       (uint)(iVar22 == 0x3f3f) * 0x80 + iVar14;
  if ((uVar40 == uVar39 && uVar7 == uVar20 || param_2 == 4) && iVar31 == 0) {
    return -5;
  }
  return iVar31;
}



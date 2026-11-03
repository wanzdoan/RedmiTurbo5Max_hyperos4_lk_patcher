// ffff0000907280b0  size=1772  FUN_ffff0000907280b0

undefined4 FUN_ffff0000907280b0(long *param_1)

{
  uint uVar1;
  uint uVar2;
  short *psVar3;
  char cVar4;
  ushort uVar5;
  ushort uVar6;
  ushort uVar7;
  ushort uVar8;
  ushort uVar9;
  ushort uVar10;
  ushort uVar11;
  ushort uVar12;
  ushort uVar13;
  undefined *puVar14;
  bool bVar15;
  uint uVar16;
  undefined4 uVar17;
  char *pcVar18;
  undefined8 uVar19;
  uint uVar20;
  uint uVar21;
  short sVar22;
  undefined1 *puVar23;
  undefined4 *puVar24;
  long lVar25;
  undefined2 uVar26;
  uint uVar27;
  uint uVar28;
  long lVar29;
  int iVar30;
  ushort uVar31;
  ushort *puVar32;
  ulong uVar33;
  ulong uVar34;
  ulong uVar35;
  
  cVar4 = *(char *)((long)param_1 + 0x3f);
  puVar23 = (undefined1 *)param_1[6];
  if (cVar4 == '\0') {
    *(undefined2 *)((long)param_1 + 0xc) = 0;
    uVar28 = *(uint *)((long)param_1 + 0x2c);
    iVar30 = *(int *)((long)param_1 + 0x1c);
    if (iVar30 != 3) {
      uVar20 = uVar28 >> 0x10 & 0xff;
      if ((uVar28 & 0xff0000) == 0) {
        uVar20 = 1;
      }
      uVar16 = uVar28 & 0xffff;
      if (((uVar16 == 0) || (uVar20 == 0)) || (uVar27 = uVar16 * uVar20, 0x10 < uVar27)) {
        pcVar18 = s__s__d__non_WRRD_transfer_length_i_ffff0000907cf478;
        uVar19 = 0x1ad;
        goto LAB_ffff000090728760;
      }
      uVar31 = 0x28;
      goto LAB_ffff000090728284;
    }
    uVar21 = uVar28 >> 0x10;
    uVar16 = uVar28 & 0xffff;
    if ((((uVar16 == 0) || (uVar21 == 0)) || (0x10 < uVar16)) || (0x10ffff < uVar28)) {
      pcVar18 = s__s__d__WRRD_transfer_length_is_n_ffff0000907edd77;
      uVar19 = 0x1ba;
      uVar20 = 2;
      goto LAB_ffff000090728764;
    }
    uVar31 = 0x38;
LAB_ffff0000907281f0:
    uVar20 = 2;
    iVar30 = 3;
    *(ushort *)((long)param_1 + 0x86) = uVar31;
    uVar16 = *(uint *)(param_1 + 5);
    *(undefined2 *)(param_1 + 0x11) = 2;
    *(short *)((long)param_1 + 0x8a) = (short)uVar28;
    *(short *)((long)param_1 + 0x8c) = (short)uVar28;
    *(short *)((long)param_1 + 0x8e) = (short)uVar21;
    uVar1 = 2;
    uVar27 = uVar28;
    if (uVar16 < 0x191) {
LAB_ffff0000907282bc:
      uVar20 = uVar1;
      if ((int)param_1[3] != 1) goto LAB_ffff0000907282d0;
    }
  }
  else {
    lVar25 = param_1[4];
    if (lVar25 == 0) {
      FUN_ffff00009077de54
                (s__s__d__I2C_d_doesnot_support_DMA_ffff0000907e5ec8,
                 s__i2c_transfer_interface_ffff0000907f0f94,0x203,(short)param_1[1]);
      return 0xfffffff8;
    }
    if ((undefined1 *)0xc0000000 < puVar23) {
      FUN_ffff00009077de54
                (s__s__d__DMA_mode_should_use_physi_ffff0000907ef5c7,
                 s__i2c_transfer_interface_ffff0000907f0f94,0x209);
      return 0xfffffff8;
    }
    *(undefined4 *)(lVar25 + 0xc) = 1;
    *(undefined4 *)(lVar25 + 0xc) = 0;
    *(undefined4 *)(lVar25 + 0xc) = 6;
    lVar29 = *param_1;
    *(undefined4 *)(lVar29 + 0x11) = 0x21;
    *(undefined4 *)(lVar25 + 0xc) = 0;
    *(undefined4 *)(lVar29 + 0x11) = 0;
    iVar30 = *(int *)((long)param_1 + 0x1c);
    uVar28 = *(uint *)((long)param_1 + 0x2c);
    *(undefined2 *)((long)param_1 + 0xc) = 0;
    if (iVar30 == 3) {
      uVar21 = uVar28 >> 0x10;
      uVar16 = uVar28 & 0xffff;
      if ((((uVar16 == 0) || (uVar21 == 0)) || (0xfffc < uVar16)) || (0x100ffff < uVar28)) {
        pcVar18 = s__s__d__DMA_WRRD_transfer_length_i_ffff0000907e4547;
        uVar19 = 0x1dd;
        uVar20 = 2;
        goto LAB_ffff000090728764;
      }
      uVar31 = 0x3c;
      goto LAB_ffff0000907281f0;
    }
    uVar20 = uVar28 >> 0x10 & 0xff;
    if ((uVar28 & 0xff0000) == 0) {
      uVar20 = 1;
    }
    uVar16 = uVar28 & 0xffff;
    if (((uVar16 == 0) || (uVar20 == 0)) || (0xfffc < uVar16)) {
      pcVar18 = s__s__d__DMA_non_WRRD_transfer_len_ffff0000908006ad;
      uVar19 = 0x1cd;
LAB_ffff000090728760:
      uVar21 = 0;
LAB_ffff000090728764:
      FUN_ffff00009077de54
                (pcVar18,s__i2c_get_transfer_len_ffff0000907d900e,uVar19,uVar16,uVar20,uVar21);
      return 0xfffffff8;
    }
    uVar27 = uVar20 * uVar28;
    uVar31 = 0x2c;
LAB_ffff000090728284:
    uVar21 = 0;
    *(ushort *)((long)param_1 + 0x86) = uVar31;
    uVar16 = *(uint *)(param_1 + 5);
    *(short *)(param_1 + 0x11) = (short)uVar20;
    *(short *)((long)param_1 + 0x8a) = (short)uVar27;
    *(short *)((long)param_1 + 0x8c) = (short)uVar28;
    *(undefined2 *)((long)param_1 + 0x8e) = 0;
    if (uVar16 < 0x191) {
      uVar1 = uVar20;
      if (uVar20 < 2) {
        uVar20 = 1;
        goto LAB_ffff0000907282d0;
      }
      goto LAB_ffff0000907282bc;
    }
  }
  uVar31 = uVar31 | 2;
  *(ushort *)((long)param_1 + 0x86) = uVar31;
LAB_ffff0000907282d0:
  puVar14 = PTR_DAT_ffff000090839968;
  uVar26 = (undefined2)uVar27;
  puVar32 = (ushort *)((long)param_1 + 0xc);
  uVar17 = 0x8001;
  if (100 < uVar16) {
    uVar17 = 0x1801;
  }
  lVar25 = *param_1;
  uVar5 = *(ushort *)(PTR_DAT_ffff000090839968 + 0x14);
  *(undefined4 *)(lVar25 + (ulong)uVar5) = uVar17;
  uVar6 = *(ushort *)(puVar14 + 0xe);
  *(undefined4 *)(lVar25 + (ulong)uVar6) = 2;
  uVar16 = uVar27 & 0xffff;
  if (*(char *)((long)param_1 + 0x41) == '\0') {
    uVar17 = 3;
  }
  else {
    uVar31 = uVar31 & 0xfff7;
    uVar17 = 0;
    *(ushort *)((long)param_1 + 0x86) = uVar31;
  }
  uVar7 = *(ushort *)(puVar14 + 0x1c);
  *(undefined4 *)(lVar25 + (ulong)uVar7) = uVar17;
  uVar8 = *(ushort *)(puVar14 + 8);
  *(uint *)(lVar25 + (ulong)uVar8) = (uint)uVar31;
  uVar33 = (ulong)*(ushort *)(puVar14 + 2);
  *(uint *)(lVar25 + uVar33) =
       (uint)(*(char *)((long)param_1 + 0x3e) != '\0') | (uint)*(byte *)(param_1 + 7) << 1;
  uVar34 = (ulong)*(ushort *)(puVar14 + 6);
  *(undefined4 *)(lVar25 + uVar34) = 7;
  uVar31 = *(ushort *)(puVar14 + 0x1a);
  *(undefined4 *)(lVar25 + (ulong)uVar31) = 5;
  uVar35 = (ulong)*(ushort *)(puVar14 + 4);
  uVar1 = *(uint *)(lVar25 + uVar35) & 0xfff8;
  uVar2 = uVar1 | 7;
  if ((char)param_1[8] != '\0') {
    uVar2 = uVar1;
  }
  *(uint *)(lVar25 + uVar35) = uVar2;
  uVar9 = *(ushort *)(puVar14 + 10);
  *(uint *)(lVar25 + (ulong)uVar9) = uVar28 & 0xffff;
  uVar10 = *(ushort *)(puVar14 + 0x2c);
  *(uint *)(lVar25 + (ulong)uVar10) = uVar21;
  uVar11 = *(ushort *)(puVar14 + 0xc);
  *(uint *)(lVar25 + (ulong)uVar11) = uVar20;
  if (cVar4 == '\0') {
    if (iVar30 != 2) {
      if ((uVar27 & 0xffff) != 0) {
        uVar12 = *(ushort *)puVar14;
        do {
          uVar16 = uVar16 - 1;
          *(undefined1 *)(*param_1 + (ulong)uVar12) = *puVar23;
          puVar23 = puVar23 + 1;
        } while (uVar16 != 0);
      }
      uVar26 = 0xffff;
    }
  }
  else {
    puVar24 = (undefined4 *)param_1[4];
    puVar24[3] = 1;
    if (iVar30 == 1) {
      lVar25 = 0x24;
      lVar29 = 0x1c;
      *puVar24 = 0;
      puVar24[6] = 0;
    }
    else if (iVar30 == 2) {
      lVar25 = 0x28;
      lVar29 = 0x20;
      *puVar24 = 0;
      puVar24[6] = 1;
    }
    else {
      *puVar24 = 0;
      lVar25 = 0x28;
      puVar24[6] = 0;
      lVar29 = 0x24;
      puVar24[7] = (int)puVar23;
      puVar24[8] = (int)puVar23;
      puVar23 = (undefined1 *)(ulong)(uVar28 & 0xffff);
      uVar16 = uVar21;
    }
    *(int *)(lVar29 + (long)puVar24) = (int)puVar23;
    *(uint *)(lVar25 + (long)puVar24) = uVar16;
    *(undefined4 *)(param_1[4] + 8) = 1;
  }
  *(undefined2 *)((long)param_1 + 0x8a) = uVar26;
  uVar12 = *(ushort *)(puVar14 + 0x12);
  lVar25 = *param_1;
  *(undefined4 *)(lVar25 + (ulong)uVar12) = 1;
  if ((char)param_1[8] != '\0') {
    lVar29 = -0xffff;
    while( true ) {
      uVar28 = *(uint *)(lVar25 + uVar34);
      uVar13 = (ushort)uVar28;
      if ((uVar28 & 6) != 0) {
        *(ushort *)((long)param_1 + 0xc) = uVar13;
        FUN_ffff00009077de54
                  (s__s__d__id__d_addr___x__transfer_e_ffff0000907e0f73,
                   s__i2c_deal_result_ffff0000907fecfd,0x113,(short)param_1[1],(char)param_1[7]);
        uVar17 = 0xffffffdd;
        uVar13 = *puVar32;
        goto joined_r0xffff00009072853c;
      }
      if ((uVar28 & 1) != 0) break;
      bVar15 = lVar29 == -1;
      lVar29 = lVar29 + 1;
      if (bVar15) {
        *(ushort *)((long)param_1 + 0xc) = uVar13;
        FUN_ffff00009077de54
                  (s__s__d__id__d_addr___x__transfer_t_ffff0000907fceaf,
                   s__i2c_deal_result_ffff0000907fecfd,0x10f,(short)param_1[1],(char)param_1[7]);
        uVar17 = 0xfffffff3;
        uVar13 = *puVar32;
joined_r0xffff00009072853c:
        if ((uVar13 >> 2 & 1) != 0) {
          FUN_ffff00009077de54
                    (s__s__d__I2C_HS_NACKERR_ffff0000907ddc85,s__i2c_deal_result_ffff0000907fecfd,
                     0x117);
          uVar13 = *puVar32;
        }
        if ((uVar13 >> 1 & 1) == 0) {
          cVar4 = *(char *)((long)param_1 + 0x42);
        }
        else {
          FUN_ffff00009077de54
                    (s__s__d__I2C_ACKERR_ffff0000907da8b0,s__i2c_deal_result_ffff0000907fecfd,0x119)
          ;
          cVar4 = *(char *)((long)param_1 + 0x42);
        }
        if (cVar4 == '\0') {
          FUN_ffff00009077de54
                    (s__s__d__I2C_structure__Clk__d_Id__ffff0000907eddce,
                     s__i2c_dump_info_ffff0000907dc4ff,0xc1,(int)param_1[2],(short)param_1[1],
                     (int)param_1[3],*(undefined1 *)((long)param_1 + 0x3f),
                     *(undefined4 *)((long)param_1 + 0x1c),(char)param_1[8],
                     *(undefined2 *)((long)param_1 + 0xc),*(undefined2 *)((long)param_1 + 0x8c),
                     (short)param_1[0x11],*(undefined2 *)((long)param_1 + 0x8e),
                     *(undefined2 *)((long)param_1 + 0x8a),(int)param_1[5]);
          FUN_ffff00009077de54
                    (s__s__d__base_address_0x_lx_ffff0000907ef5fd,s__i2c_dump_info_ffff0000907dc4ff,
                     0xc3,*param_1);
          lVar25 = *param_1;
          FUN_ffff00009077de54
                    (s__s__d__I2C_register__SLAVE_ADDR__ffff0000907f96ff,
                     s__i2c_dump_info_ffff0000907dc4ff,0xd8,*(undefined4 *)(lVar25 + uVar33),
                     *(undefined4 *)(lVar25 + uVar35),*(undefined4 *)(lVar25 + uVar34),
                     *(undefined4 *)(lVar25 + (ulong)uVar8),*(undefined4 *)(lVar25 + (ulong)uVar9),
                     *(undefined4 *)(lVar25 + (ulong)uVar11),*(undefined4 *)(lVar25 + (ulong)uVar6),
                     *(undefined4 *)(lVar25 + (ulong)*(ushort *)(puVar14 + 0x10)),
                     *(undefined4 *)(lVar25 + (ulong)uVar12),
                     *(undefined4 *)(lVar25 + (ulong)*(ushort *)(puVar14 + 0x16)),
                     *(undefined4 *)(lVar25 + (ulong)uVar7),
                     *(undefined4 *)(lVar25 + (ulong)*(ushort *)(puVar14 + 0x20)),
                     *(undefined4 *)(lVar25 + (ulong)*(ushort *)(puVar14 + 0x24)),
                     *(undefined4 *)(lVar25 + (ulong)*(ushort *)(puVar14 + 0x28)),
                     *(undefined4 *)(lVar25 + (ulong)uVar5),*(undefined4 *)(lVar25 + (ulong)uVar10))
          ;
        }
        lVar25 = *param_1;
        *(undefined4 *)(lVar25 + (ulong)*(ushort *)(puVar14 + 0x22)) = 1;
        *(undefined4 *)(lVar25 + uVar33) = 0;
        *(undefined4 *)(lVar25 + uVar34) = 7;
        *(undefined4 *)(lVar25 + (ulong)uVar31) = 5;
        return uVar17;
      }
    }
    *puVar32 = uVar13;
  }
  if ((*(char *)((long)param_1 + 0x3f) == '\0') &&
     ((*(uint *)((long)param_1 + 0x1c) & 0xfffffffe) == 2)) {
    psVar3 = (short *)((long)param_1 + 0x8e);
    if (*(uint *)((long)param_1 + 0x1c) != 3) {
      psVar3 = (short *)((long)param_1 + 0x8c);
    }
    sVar22 = *psVar3;
    if (sVar22 != 0) {
      uVar31 = *(ushort *)puVar14;
      puVar23 = (undefined1 *)param_1[6];
      do {
        sVar22 = sVar22 + -1;
        *puVar23 = *(undefined1 *)(*param_1 + (ulong)uVar31);
        puVar23 = puVar23 + 1;
      } while (sVar22 != 0);
      return 0;
    }
  }
  return 0;
}



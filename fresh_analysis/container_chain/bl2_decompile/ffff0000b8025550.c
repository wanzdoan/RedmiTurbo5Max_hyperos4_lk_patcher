
void FUN_ffff0000b8025550(long param_1)

{
  uint *puVar1;
  uint uVar2;
  bool bVar3;
  int iVar4;
  int iVar5;
  long lVar6;
  char *pcVar7;
  ulong uVar8;
  uint uVar9;
  ulong uVar10;
  uint *puVar11;
  long lVar12;
  uint *puVar13;
  ulong uVar14;
  uint uVar15;
  uint local_b4;
  uint local_b0 [8];
  undefined8 local_90;
  int local_88 [9];
  uint local_64;
  
  local_88[4] = 0;
  local_88[5] = 0;
  local_88[6] = 0;
  local_88[7] = 0;
  local_88[0] = 0;
  local_88[1] = 0;
  local_88[2] = 0;
  local_88[3] = 0;
  local_90 = 0;
  local_b0[4] = 0;
  local_b0[5] = 0;
  local_b0[6] = 0;
  local_b0[7] = 0;
  local_b0[0] = 0;
  local_b0[1] = 0;
  local_b0[2] = 0;
  local_b0[3] = 0;
  if (param_1 == 0) {
    FUN_ffff0000b804c33c(s__SPI__ERR___lk_driver_fdt_is_NUL_ffff0000b809466a);
    return;
  }
  uVar15 = 0;
  iVar5 = -1;
  local_b4 = 0;
  DAT_ffff0000b80e9d50 = 0;
  do {
    lVar6 = FUN_ffff0000b804bccc(0x28);
    (&DAT_ffff0000b811cd50)[(int)DAT_ffff0000b80e9d50] = (undefined8 *)lVar6;
    if (lVar6 == 0) {
      FUN_ffff0000b804c33c(s__SPI__ERR___failed_to_malloc_mtk_ffff0000b8098da5);
      return;
    }
    FUN_ffff0000b804d070(lVar6,0,0x28);
    iVar4 = FUN_ffff0000b80483cc(param_1,iVar5,s_mediatek_mt6991_spi_ffff0000b80e9d58);
    local_88[(int)DAT_ffff0000b80e9d50] = iVar4;
    if (iVar4 < 1) {
      iVar4 = FUN_ffff0000b80483cc(param_1,iVar5,s_mediatek_mt6989_spi_ffff0000b80e9de0);
      local_88[(int)DAT_ffff0000b80e9d50] = iVar4;
      if (0 < iVar4) {
        lVar6 = (long)(int)DAT_ffff0000b80e9d50;
        lVar12 = 1;
        goto LAB_ffff0000b80256f8;
      }
      iVar4 = FUN_ffff0000b80483cc(param_1,iVar5,s_mediatek_mt6985_spi_ffff0000b80e9e68);
      local_88[(int)DAT_ffff0000b80e9d50] = iVar4;
      if (0 < iVar4) {
        lVar6 = (long)(int)DAT_ffff0000b80e9d50;
        lVar12 = 2;
        goto LAB_ffff0000b80256f8;
      }
      iVar4 = FUN_ffff0000b80483cc(param_1,iVar5,s_mediatek_mt6983_spi_ffff0000b80e9ef0);
      local_88[(int)DAT_ffff0000b80e9d50] = iVar4;
      if (0 < iVar4) {
        lVar6 = (long)(int)DAT_ffff0000b80e9d50;
        lVar12 = 3;
        goto LAB_ffff0000b80256f8;
      }
      iVar5 = FUN_ffff0000b80483cc(param_1,iVar5,s_mediatek_mt6899_spi_ffff0000b80e9f78);
      local_88[(int)DAT_ffff0000b80e9d50] = iVar5;
      uVar9 = DAT_ffff0000b80e9d50;
      if (0 < iVar5) {
        lVar6 = (long)(int)DAT_ffff0000b80e9d50;
        lVar12 = 4;
        goto LAB_ffff0000b80256f8;
      }
    }
    else {
      lVar12 = 0;
      lVar6 = (long)(int)DAT_ffff0000b80e9d50;
LAB_ffff0000b80256f8:
      uVar9 = DAT_ffff0000b80e9d50;
      *(undefined **)((long)(&DAT_ffff0000b811cd50)[lVar6] + 0x20) =
           (&PTR_PTR_ffff0000b80e9dd8)[lVar12 * 0x11];
    }
    if (local_88[0] < 1) {
      FUN_ffff0000b804c33c(s__SPI__ERR___failed_to_match_comp_ffff0000b80864c9);
      return;
    }
    iVar5 = local_88[(int)uVar9];
    if (iVar5 < 1) {
      return;
    }
    DAT_ffff0000b811cd48 = (undefined1)uVar9;
    local_64 = 0;
    lVar6 = FUN_ffff0000b804803c(param_1,iVar5,&DAT_ffff0000b808dfe3,&local_64);
    if (((int)local_64 < 1) || (lVar6 == 0)) {
      uVar8 = (ulong)DAT_ffff0000b80e9d50;
      pcVar7 = s__SPI__ERR___spi__d__do_not_find_r_ffff0000b808aa2f;
LAB_ffff0000b80258f0:
      FUN_ffff0000b804c33c(pcVar7,uVar8);
    }
    else {
      uVar9 = local_64 >> 2;
      bVar3 = 3 < local_64;
      local_64 = uVar9;
      if (bVar3) {
        if (uVar9 < 2) {
          uVar9 = 1;
        }
        uVar8 = (ulong)uVar9;
        if (uVar9 < 2) {
          uVar10 = 0;
LAB_ffff0000b80257bc:
          lVar12 = uVar8 - uVar10;
          puVar11 = local_b0 + uVar10;
          puVar13 = (uint *)(lVar6 + uVar10 * 4);
          do {
            lVar12 = lVar12 + -1;
            uVar15 = (*puVar13 & 0xff00ff00) >> 8 | (*puVar13 & 0xff00ff) << 8;
            *puVar11 = uVar15 >> 0x10 | uVar15 << 0x10;
            puVar11 = puVar11 + 1;
            puVar13 = puVar13 + 1;
          } while (lVar12 != 0);
        }
        else {
          uVar10 = uVar8 & 0x3ffffffe;
          puVar11 = (uint *)(lVar6 + 4);
          uVar14 = uVar10;
          puVar13 = (uint *)((ulong)local_b0 | 4);
          do {
            puVar1 = puVar11 + -1;
            uVar15 = *puVar11;
            puVar11 = puVar11 + 2;
            uVar14 = uVar14 - 2;
            uVar9 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
            uVar15 = (uVar15 & 0xff00ff00) >> 8 | (uVar15 & 0xff00ff) << 8;
            puVar13[-1] = uVar9 >> 0x10 | uVar9 << 0x10;
            *puVar13 = uVar15 >> 0x10 | uVar15 << 0x10;
            puVar13 = puVar13 + 2;
          } while (uVar14 != 0);
          if (uVar10 != uVar8) goto LAB_ffff0000b80257bc;
        }
        uVar15 = local_b0[1];
      }
      uVar8 = (ulong)(int)DAT_ffff0000b80e9d50;
      if (uVar15 != *(int *)(**(long **)((long)(&DAT_ffff0000b811cd50)[uVar8] + 0x20) + uVar8 * 8))
      {
        pcVar7 = s__SPI__ERR___the_SPI__d__arrangem_ffff0000b808dfe7;
        goto LAB_ffff0000b80258f0;
      }
      local_64 = 0;
      lVar6 = FUN_ffff0000b804803c
                        (param_1,local_88[uVar8],s_mediatek_pad_select_ffff0000b80972bb,&local_64);
      if (((int)local_64 < 1) || (lVar6 == 0)) {
        uVar8 = (ulong)DAT_ffff0000b80e9d50;
        pcVar7 = s__SPI__ERR___spi__d__do_not_find_p_ffff0000b8098dd3;
        goto LAB_ffff0000b80258f0;
      }
      uVar9 = local_64 >> 2;
      bVar3 = 3 < local_64;
      local_64 = uVar9;
      if (bVar3) {
        if (uVar9 < 2) {
          uVar9 = 1;
        }
        uVar9 = *(uint *)(lVar6 + (ulong)uVar9 * 4 + -4);
        uVar9 = (uVar9 & 0xff00ff00) >> 8 | (uVar9 & 0xff00ff) << 8;
        local_b4 = uVar9 >> 0x10 | uVar9 << 0x10;
      }
      uVar8 = (ulong)DAT_ffff0000b80e9d50;
      if (local_b4 < 2) {
        lVar6 = (long)(int)DAT_ffff0000b80e9d50;
        lVar12 = (long)(&DAT_ffff0000b811cd50)[lVar6];
        *(uint *)(lVar12 + 0x10) = local_b4;
        if (*(char *)(*(long *)(lVar12 + 0x20) + 10) != '\0') {
          local_64 = 0;
          lVar6 = FUN_ffff0000b804803c
                            (param_1,local_88[lVar6],s_mediatek_tickdly_ffff0000b80926b8,&local_64);
          if (((int)local_64 < 1) || (lVar6 == 0)) {
            uVar8 = (ulong)DAT_ffff0000b80e9d50;
            pcVar7 = s__SPI__ERR___spi__d__do_not_find_t_ffff0000b809193b;
            goto LAB_ffff0000b80258f0;
          }
          uVar9 = local_64 >> 2;
          bVar3 = 3 < local_64;
          local_64 = uVar9;
          if (bVar3) {
            if (uVar9 < 2) {
              uVar9 = 1;
            }
            uVar8 = (ulong)uVar9;
            if (uVar9 < 2) {
              uVar10 = 0;
            }
            else {
              uVar10 = uVar8 & 0x3ffffffe;
              puVar11 = (uint *)(lVar6 + 4);
              uVar14 = uVar10;
              puVar13 = (uint *)((ulong)&local_90 | 4);
              do {
                puVar1 = puVar11 + -1;
                uVar9 = *puVar11;
                puVar11 = puVar11 + 2;
                uVar14 = uVar14 - 2;
                uVar2 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
                uVar9 = (uVar9 & 0xff00ff00) >> 8 | (uVar9 & 0xff00ff) << 8;
                puVar13[-1] = uVar2 >> 0x10 | uVar2 << 0x10;
                *puVar13 = uVar9 >> 0x10 | uVar9 << 0x10;
                puVar13 = puVar13 + 2;
              } while (uVar14 != 0);
              if (uVar10 == uVar8) goto LAB_ffff0000b802598c;
            }
            lVar12 = uVar8 - uVar10;
            puVar11 = (uint *)((long)&local_90 + uVar10 * 4);
            puVar13 = (uint *)(lVar6 + uVar10 * 4);
            do {
              lVar12 = lVar12 + -1;
              uVar9 = (*puVar13 & 0xff00ff00) >> 8 | (*puVar13 & 0xff00ff) << 8;
              *puVar11 = uVar9 >> 0x10 | uVar9 << 0x10;
              puVar11 = puVar11 + 1;
              puVar13 = puVar13 + 1;
            } while (lVar12 != 0);
          }
LAB_ffff0000b802598c:
          uVar8 = (ulong)(int)DAT_ffff0000b80e9d50;
          uVar2 = *(uint *)((long)&local_90 + (ulong)local_b4 * 4);
          uVar9 = 8;
          if (*(char *)(*(long *)((long)(&DAT_ffff0000b811cd50)[uVar8] + 0x20) + 9) != '\0') {
            uVar9 = 0x80;
          }
          if (uVar9 <= uVar2) {
            pcVar7 = s__SPI__ERR___spi__d__tickdly_is_i_ffff0000b80a11dc;
            goto LAB_ffff0000b8025914;
          }
          *(uint *)((long)(&DAT_ffff0000b811cd50)[uVar8] + (ulong)local_b4 * 4 + 0x14) = uVar2;
        }
        switch(uVar8 & 0xffffffff) {
        case 0:
          *DAT_ffff0000b811cd50 = 0xffff000016110000;
          break;
        case 1:
          *DAT_ffff0000b811cd58 = 0xffff000016130000;
          break;
        case 2:
          *DAT_ffff0000b811cd60 = 0xffff000016150000;
          break;
        case 3:
          *DAT_ffff0000b811cd68 = 0xffff000016170000;
          break;
        case 4:
          *DAT_ffff0000b811cd70 = 0xffff000016190000;
          break;
        case 5:
          *DAT_ffff0000b811cd78 = 0xffff0000161b0000;
          break;
        case 6:
          *DAT_ffff0000b811cd80 = 0xffff0000161d0000;
          break;
        case 7:
          *DAT_ffff0000b811cd88 = 0xffff0000161f0000;
          break;
        default:
          pcVar7 = s__SPI__ERR___get_spi__d__baseaddr_ffff0000b809361c;
          goto LAB_ffff0000b80258f0;
        }
      }
      else {
        pcVar7 = s__SPI__ERR___spi__d__pad_select_i_ffff0000b80a11a6;
LAB_ffff0000b8025914:
        FUN_ffff0000b804c33c(pcVar7);
      }
    }
    uVar9 = DAT_ffff0000b80e9d50 + 1;
    bVar3 = 6 < (int)DAT_ffff0000b80e9d50;
    DAT_ffff0000b80e9d50 = uVar9;
    if (bVar3) {
      return;
    }
  } while( true );
}



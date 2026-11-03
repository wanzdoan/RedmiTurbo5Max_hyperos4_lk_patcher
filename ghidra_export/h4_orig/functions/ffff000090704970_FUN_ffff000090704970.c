// ffff000090704970  size=1040  FUN_ffff000090704970

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090704970(void)

{
  long lVar1;
  byte bVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  char *pcVar8;
  undefined8 uVar9;
  undefined *puVar10;
  ulong uVar11;
  long lVar12;
  long lVar13;
  undefined **ppuVar14;
  ulong uVar15;
  undefined4 local_68;
  uint local_64;
  
  lVar7 = FUN_ffff00009077d514(0x400);
  if (lVar7 == 0) {
    iVar4 = -0xc;
  }
  else {
    local_68 = 0;
    FUN_ffff00009077ee50(lVar7,0,0x400);
    local_64 = 0;
    iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x10f,&local_64);
    ppuVar14 = &PTR_DAT_ffff000090816440;
    if (iVar4 == 0) {
      iVar6 = 0;
      uVar15 = 7;
      lVar13 = 0;
      do {
        lVar12 = lVar13;
        bVar2 = *(byte *)(uVar15 * 4 + -0xffff6f7fc356);
        uVar3 = (bVar2 & 0xaaaaaaaa) >> 1 | (bVar2 & 0x55555555) << 1;
        uVar3 = (uVar3 & 0xcccccccc) >> 2 | (uVar3 & 0x33333333) << 2;
        uVar11 = ((ulong)local_64 & (ulong)bVar2) >>
                 LZCOUNT((uVar3 >> 4 | (uVar3 & 0xf0f0f0f) << 4) << 0x18);
        if (((uint)uVar15 < 0xe) && ((1 << (ulong)((uint)uVar15 & 0x1f) & 0x21d2U) != 0)) {
          lVar13 = uVar15 * 0x20;
          lVar1 = 0;
          if ((uint)*(byte *)(lVar13 + -0xffff6f7fc56c) <= (uint)uVar11) {
            lVar1 = uVar11 - *(byte *)(lVar13 + -0xffff6f7fc56c);
          }
          uVar11 = (ulong)*(uint *)(lVar13 + -0xffff6f7fc578) +
                   lVar1 * (ulong)*(uint *)(lVar13 + -0xffff6f7fc570);
          if (0xfffffffe < uVar11) {
            uVar11 = 0xffffffff;
          }
        }
        lVar13 = lVar7 + iVar6;
        if (lVar12 + 0x18 == 0x90) {
          iVar5 = FUN_ffff00009077f6fc
                            (lVar13,(long)(0x400 - iVar6),s__s____d_s_ffff0000907ef2f6,
                             *(undefined8 *)((long)&PTR_DAT_ffff000090816440 + lVar12),uVar11,
                             &DAT_ffff0000907d2431);
          iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x70,&local_68);
          if (iVar4 == 0) {
            lVar13 = (long)iVar6 + (long)iVar5;
            iVar5 = (int)lVar13;
            iVar6 = FUN_ffff00009077f6fc
                              (lVar7 + lVar13,(long)(0x400 - iVar5),s__s___0x_02X__ffff0000907fca5e,
                               s_CHG_STAT0_ffff0000907f794d,local_68);
            iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x71,&local_68);
            if (iVar4 == 0) {
              iVar6 = iVar6 + iVar5;
              iVar5 = FUN_ffff00009077f6fc
                                (lVar7 + iVar6,(long)(0x400 - iVar6),s__s___0x_02X__ffff0000907fca5e
                                 ,s_CHG_STAT1_ffff0000908002cd,local_68);
              iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x107,&local_68);
              if (iVar4 == 0) {
                iVar5 = iVar5 + iVar6;
                iVar6 = FUN_ffff00009077f6fc
                                  (lVar7 + iVar5,(long)(0x400 - iVar5),
                                   s__s___0x_02X__ffff0000907fca5e,s_CHG_TOP1_ffff0000907f3dd1,
                                   local_68);
                iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x108,&local_68);
                if (iVar4 == 0) {
                  iVar6 = iVar6 + iVar5;
                  iVar5 = FUN_ffff00009077f6fc
                                    (lVar7 + iVar6,(long)(0x400 - iVar6),
                                     s__s___0x_02X__ffff0000907fca5e,s_CHG_TOP2_ffff0000908002d7,
                                     local_68);
                  iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x111,&local_68);
                  if (iVar4 == 0) {
                    iVar5 = iVar5 + iVar6;
                    iVar6 = FUN_ffff00009077f6fc
                                      (lVar7 + iVar5,(long)(0x400 - iVar5),
                                       s__s___0x_02X__ffff0000907fca5e,s_CHG_EOC1_ffff0000907da4a3,
                                       local_68);
                    iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x112,&local_68);
                    if (iVar4 == 0) {
                      iVar6 = iVar6 + iVar5;
                      iVar5 = FUN_ffff00009077f6fc
                                        (lVar7 + iVar6,(long)(0x400 - iVar6),
                                         s__s___0x_02X__ffff0000907fca5e,s_CHG_EOC2_ffff0000907dbe8c
                                         ,local_68);
                      iVar4 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x114,&local_68);
                      if (iVar4 == 0) {
                        FUN_ffff00009077f6fc
                                  (lVar7 + (iVar5 + iVar6),(long)(0x400 - (iVar5 + iVar6)),
                                   s__s___0x_02X_ffff0000907f3dda,s_CHG_WDT_ffff0000907ebd6d,
                                   local_68);
                        goto LAB_ffff000090704af4;
                      }
                      lVar13 = 6;
                    }
                    else {
                      lVar13 = 5;
                    }
                  }
                  else {
                    lVar13 = 4;
                  }
                }
                else {
                  lVar13 = 3;
                }
              }
              else {
                lVar13 = 2;
              }
            }
            else {
              lVar13 = 1;
            }
          }
          else {
            lVar13 = 0;
          }
          pcVar8 = s__s__d__Failed_to_read__s_ffff0000907d56aa;
          uVar9 = 0x310;
          puVar10 = (&PTR_s_CHG_STAT0_ffff0000908164d8)[lVar13 * 2];
          goto LAB_ffff000090704af0;
        }
        iVar4 = FUN_ffff00009077f6fc
                          (lVar13,(long)(0x400 - iVar6),s__s____d_s__ffff0000907d2434,
                           *(undefined8 *)((long)&PTR_DAT_ffff000090816440 + lVar12),uVar11,
                           *(undefined8 *)((long)&PTR_DAT_ffff000090816448 + lVar12));
        uVar15 = (ulong)*(uint *)((long)&DAT_ffff000090816468 + lVar12);
        iVar6 = iVar4 + iVar6;
        local_64 = 0;
        iVar4 = FUN_ffff000090737554
                          (_DAT_ffff00009083d080,*(undefined2 *)(uVar15 * 4 + -0xffff6f7fc358),
                           &local_64);
        lVar13 = lVar12 + 0x18;
      } while (iVar4 == 0);
      ppuVar14 = (undefined **)((long)&PTR_DAT_ffff000090816458 + lVar12);
    }
    puVar10 = *ppuVar14;
    pcVar8 = s__s__d__Failed_to_get__s_ffff0000907e8b80;
    uVar9 = 0x2fe;
LAB_ffff000090704af0:
    FUN_ffff00009077e11c(pcVar8,s__mt6379_charger_dump_register_ffff0000907d3d27,uVar9,puVar10);
LAB_ffff000090704af4:
    thunk_FUN_ffff0000907c314c(lVar7);
  }
  return iVar4;
}



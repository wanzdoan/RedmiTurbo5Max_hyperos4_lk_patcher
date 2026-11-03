// ffff000090704a14  size=1040  FUN_ffff000090704a14

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090704a14(void)

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
  
  lVar7 = FUN_ffff00009077d24c(0x400);
  if (lVar7 == 0) {
    iVar4 = -0xc;
  }
  else {
    local_68 = 0;
    FUN_ffff00009077eb88(lVar7,0,0x400);
    local_64 = 0;
    iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x10f,&local_64);
    ppuVar14 = &PTR_DAT_ffff000090816440;
    if (iVar4 == 0) {
      iVar6 = 0;
      uVar15 = 7;
      lVar13 = 0;
      do {
        lVar12 = lVar13;
        bVar2 = *(byte *)(uVar15 * 4 + -0xffff6f7fc57e);
        uVar3 = (bVar2 & 0xaaaaaaaa) >> 1 | (bVar2 & 0x55555555) << 1;
        uVar3 = (uVar3 & 0xcccccccc) >> 2 | (uVar3 & 0x33333333) << 2;
        uVar11 = ((ulong)local_64 & (ulong)bVar2) >>
                 LZCOUNT((uVar3 >> 4 | (uVar3 & 0xf0f0f0f) << 4) << 0x18);
        if (((uint)uVar15 < 0xe) && ((1 << (ulong)((uint)uVar15 & 0x1f) & 0x21d2U) != 0)) {
          lVar13 = uVar15 * 0x20;
          lVar1 = 0;
          if ((uint)*(byte *)(lVar13 + -0xffff6f7fc794) <= (uint)uVar11) {
            lVar1 = uVar11 - *(byte *)(lVar13 + -0xffff6f7fc794);
          }
          uVar11 = (ulong)*(uint *)(lVar13 + -0xffff6f7fc7a0) +
                   lVar1 * (ulong)*(uint *)(lVar13 + -0xffff6f7fc798);
          if (0xfffffffe < uVar11) {
            uVar11 = 0xffffffff;
          }
        }
        lVar13 = lVar7 + iVar6;
        if (lVar12 + 0x18 == 0x90) {
          iVar5 = FUN_ffff00009077f434
                            (lVar13,(long)(0x400 - iVar6),s__s____d_s_ffff0000907ef185,
                             *(undefined8 *)((long)&PTR_DAT_ffff000090816440 + lVar12),uVar11,
                             &DAT_ffff0000907d23f4);
          iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x70,&local_68);
          if (iVar4 == 0) {
            lVar13 = (long)iVar6 + (long)iVar5;
            iVar5 = (int)lVar13;
            iVar6 = FUN_ffff00009077f434
                              (lVar7 + lVar13,(long)(0x400 - iVar5),s__s___0x_02X__ffff0000907fc8c2,
                               s_CHG_STAT0_ffff0000907f77c3,local_68);
            iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x71,&local_68);
            if (iVar4 == 0) {
              iVar6 = iVar6 + iVar5;
              iVar5 = FUN_ffff00009077f434
                                (lVar7 + iVar6,(long)(0x400 - iVar6),s__s___0x_02X__ffff0000907fc8c2
                                 ,s_CHG_STAT1_ffff000090800125,local_68);
              iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x107,&local_68);
              if (iVar4 == 0) {
                iVar5 = iVar5 + iVar6;
                iVar6 = FUN_ffff00009077f434
                                  (lVar7 + iVar5,(long)(0x400 - iVar5),
                                   s__s___0x_02X__ffff0000907fc8c2,s_CHG_TOP1_ffff0000907f3c45,
                                   local_68);
                iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x108,&local_68);
                if (iVar4 == 0) {
                  iVar6 = iVar6 + iVar5;
                  iVar5 = FUN_ffff00009077f434
                                    (lVar7 + iVar6,(long)(0x400 - iVar6),
                                     s__s___0x_02X__ffff0000907fc8c2,s_CHG_TOP2_ffff00009080012f,
                                     local_68);
                  iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x111,&local_68);
                  if (iVar4 == 0) {
                    iVar5 = iVar5 + iVar6;
                    iVar6 = FUN_ffff00009077f434
                                      (lVar7 + iVar5,(long)(0x400 - iVar5),
                                       s__s___0x_02X__ffff0000907fc8c2,s_CHG_EOC1_ffff0000907da40c,
                                       local_68);
                    iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x112,&local_68);
                    if (iVar4 == 0) {
                      iVar6 = iVar6 + iVar5;
                      iVar5 = FUN_ffff00009077f434
                                        (lVar7 + iVar6,(long)(0x400 - iVar6),
                                         s__s___0x_02X__ffff0000907fc8c2,s_CHG_EOC2_ffff0000907dbd81
                                         ,local_68);
                      iVar4 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x114,&local_68);
                      if (iVar4 == 0) {
                        FUN_ffff00009077f434
                                  (lVar7 + (iVar5 + iVar6),(long)(0x400 - (iVar5 + iVar6)),
                                   s__s___0x_02X_ffff0000907f3c4e,s_CHG_WDT_ffff0000907ebbea,
                                   local_68);
                        goto LAB_ffff000090704b98;
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
          pcVar8 = s__s__d__Failed_to_read__s_ffff0000907d566d;
          uVar9 = 0x310;
          puVar10 = (&PTR_s_CHG_STAT0_ffff0000908164d8)[lVar13 * 2];
          goto LAB_ffff000090704b94;
        }
        iVar4 = FUN_ffff00009077f434
                          (lVar13,(long)(0x400 - iVar6),s__s____d_s__ffff0000907d23f7,
                           *(undefined8 *)((long)&PTR_DAT_ffff000090816440 + lVar12),uVar11,
                           *(undefined8 *)((long)&PTR_DAT_ffff000090816448 + lVar12));
        uVar15 = (ulong)*(uint *)((long)&DAT_ffff000090816468 + lVar12);
        iVar6 = iVar4 + iVar6;
        local_64 = 0;
        iVar4 = FUN_ffff0000907372bc
                          (_DAT_ffff00009083d0e0,*(undefined2 *)(uVar15 * 4 + -0xffff6f7fc580),
                           &local_64);
        lVar13 = lVar12 + 0x18;
      } while (iVar4 == 0);
      ppuVar14 = (undefined **)((long)&PTR_DAT_ffff000090816458 + lVar12);
    }
    puVar10 = *ppuVar14;
    pcVar8 = s__s__d__Failed_to_get__s_ffff0000907e8a3c;
    uVar9 = 0x2fe;
LAB_ffff000090704b94:
    FUN_ffff00009077de54(pcVar8,s__mt6379_charger_dump_register_ffff0000907d3cea,uVar9,puVar10);
LAB_ffff000090704b98:
    thunk_FUN_ffff0000907c2ad8(lVar7);
  }
  return iVar4;
}



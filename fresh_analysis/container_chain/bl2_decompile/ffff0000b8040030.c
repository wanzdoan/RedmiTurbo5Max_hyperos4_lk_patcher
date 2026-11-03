
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8040030(long param_1)

{
  undefined1 uVar1;
  undefined1 uVar2;
  undefined1 uVar3;
  undefined1 uVar4;
  uint uVar5;
  int iVar6;
  long lVar7;
  long lVar8;
  char cVar9;
  long lVar10;
  undefined1 *puVar11;
  ulong uVar12;
  char *pcVar13;
  int local_14;
  
  _DAT_ffff00001002d6f0 = _DAT_ffff00001002d6f0 & 0xfffffcff | 3;
  lVar7 = FUN_ffff0000b804bccc(0x65a);
  if (lVar7 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b744(s__GPIO__Fail_to_alloc_mem_ffff0000b8089472);
  }
  FUN_ffff0000b804d070(lVar7,0,0x65a);
  if (param_1 != 0) {
    iVar6 = FUN_ffff0000b8047d28(param_1,s__soc_gpio_1002d000_ffff0000b8097771);
    if (-1 < iVar6) {
      local_14 = 0;
      lVar8 = FUN_ffff0000b804803c(param_1,iVar6,s_gpio_init_default_ffff0000b8092bee,&local_14);
      if ((0 < local_14) && (lVar8 != 0)) {
        lVar10 = 0;
        do {
          puVar11 = (undefined1 *)(lVar7 + lVar10);
          lVar10 = lVar10 + 0xc;
          *puVar11 = 0xff;
          puVar11[6] = 0xff;
        } while (lVar10 != 0x654);
        *(undefined1 *)(lVar7 + 0x654) = 0xff;
        if (local_14 * -0x49249249 + 0x12492492U < 0x24924925) {
          if (0x1b < local_14) {
            lVar8 = lVar8 + 0xc;
            iVar6 = local_14 / 0x1c;
            do {
              uVar5 = (*(uint *)(lVar8 + -0xc) & 0xff00ff00) >> 8 |
                      (*(uint *)(lVar8 + -0xc) & 0xff00ff) << 8;
              uVar5 = uVar5 >> 0x10 | uVar5 << 0x10;
              if ((int)uVar5 < 0x10f) {
                puVar11 = (undefined1 *)(lVar7 + (long)(int)uVar5 * 6);
                uVar4 = *(undefined1 *)(lVar8 + -1);
                uVar1 = *(undefined1 *)(lVar8 + 3);
                uVar2 = *(undefined1 *)(lVar8 + 7);
                *puVar11 = *(undefined1 *)(lVar8 + -5);
                uVar3 = *(undefined1 *)(lVar8 + 0xb);
                puVar11[1] = uVar4;
                uVar4 = *(undefined1 *)(lVar8 + 0xf);
                puVar11[4] = uVar1;
                puVar11[2] = uVar2;
                puVar11[3] = uVar3;
                puVar11[5] = uVar4;
              }
              iVar6 = iVar6 + -1;
              lVar8 = lVar8 + 0x1c;
            } while (iVar6 != 0);
          }
          uVar12 = 0;
          pcVar13 = (char *)(lVar7 + 2);
          local_14 = local_14 / 0x1c;
          do {
            if (pcVar13[-2] != -1) {
              FUN_ffff0000b805098c(uVar12 & 0xffffffff);
              FUN_ffff0000b8050878(uVar12 & 0xffffffff,pcVar13[-1]);
              if (*pcVar13 == '\0') {
                cVar9 = '\0';
                pcVar13[1] = '\0';
              }
              else {
                cVar9 = pcVar13[1];
              }
              FUN_ffff0000b8050c44(uVar12 & 0xffffffff,cVar9);
              FUN_ffff0000b8050a5c(uVar12 & 0xffffffff,*pcVar13);
              FUN_ffff0000b80508e8(uVar12 & 0xffffffff,pcVar13[2]);
              FUN_ffff0000b80509f0(uVar12 & 0xffffffff,pcVar13[3]);
            }
            uVar12 = uVar12 + 1;
            pcVar13 = pcVar13 + 6;
          } while (uVar12 != 0x10f);
          thunk_FUN_ffff0000b8082558(lVar7);
          return;
        }
      }
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b744(s__GPIO__fall_back_to_default_ffff0000b8087845);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b744(s__GPIO__null_fdt_ffff0000b80968d7);
}



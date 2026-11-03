// ffff000090708cf4  size=564  FUN_ffff000090708cf4

void FUN_ffff000090708cf4
               (ulong param_1,ulong param_2,ulong param_3,ulong param_4,uint param_5,long param_6,
               long param_7)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  long *plVar4;
  long lVar5;
  ulong uVar6;
  ulong uVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  
  if (param_3 != 0) {
    uVar3 = param_5 - 3;
    uVar7 = 1L << (param_4 & 0x3f);
    uVar2 = 1 << (ulong)(uVar3 & 0x1f);
    lVar8 = (long)(int)uVar2;
    if ((int)uVar2 < 2) {
      uVar2 = 1;
    }
    do {
      uVar10 = param_2 >> (param_4 & 0x3f);
      uVar11 = param_2 & uVar7 - 1;
      uVar9 = uVar7 - uVar11;
      uVar6 = *(ulong *)(param_6 + uVar10 * 8);
      uVar1 = param_3;
      if (uVar9 <= param_3) {
        uVar1 = uVar9;
      }
      if ((param_5 < (uint)param_4) && ((uVar6 & 3) == 3)) {
        uVar6 = uVar6 & 0xfffffffff000;
        plVar4 = (long *)FUN_ffff00009077b2cc(uVar6);
        FUN_ffff000090708cf4(param_1,uVar11,uVar1,(uint)param_4 - uVar3,param_5,plVar4,param_7);
        if ((uVar3 == 0x1f) || (uVar1 == uVar7)) {
LAB_ffff000090708e60:
          *(undefined8 *)(param_6 + uVar10 * 8) = 0;
          uVar9 = param_1 >> 0xc & 0xfffffffffff;
          DataMemoryBarrier(2,2);
          if ((int)param_7 == -1) {
            TLBI_VAAE1IS(uVar9);
            InstructionSynchronizationBarrier();
          }
          else {
            uVar9 = uVar9 | param_7 << 0x30;
            TLBI_VAE1IS(uVar9);
            InstructionSynchronizationBarrier();
          }
          if (param_5 < 0xc) {
            thunk_FUN_ffff0000907c314c(uVar9,plVar4);
          }
          else {
            lVar5 = FUN_ffff00009077ab14(uVar6);
            if (lVar5 == 0) {
                    /* WARNING: Subroutine does not return */
              FUN_ffff00009077cee0(s_bad_page_table_paddr_0x_lx_ffff0000907d731d,uVar6);
            }
            FUN_ffff00009077afd0();
          }
        }
        else if (*plVar4 == 0) {
          uVar9 = 1;
          do {
            uVar11 = uVar9;
            if (uVar2 == uVar11) break;
            uVar9 = uVar11 + 1;
          } while (plVar4[uVar11] == 0);
          if (lVar8 <= (long)uVar11) goto LAB_ffff000090708e60;
        }
      }
      else if (uVar6 != 0) {
        *(undefined8 *)(param_6 + uVar10 * 8) = 0;
        uVar6 = param_1 >> 0xc & 0xfffffffffff;
        DataMemoryBarrier(2,2);
        if ((int)param_7 == -1) {
          TLBI_VAAE1IS(uVar6);
          InstructionSynchronizationBarrier();
        }
        else {
          TLBI_VAE1IS(uVar6 | param_7 << 0x30);
          InstructionSynchronizationBarrier();
        }
      }
      param_1 = uVar1 + param_1;
      param_2 = uVar1 + param_2;
      param_3 = param_3 - uVar1;
    } while (param_3 != 0);
  }
  return;
}



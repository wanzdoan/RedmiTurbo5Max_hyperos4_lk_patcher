// ffff000090719020  size=504  FUN_ffff000090719020

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090719020(undefined8 param_1,uint param_2)

{
  int iVar1;
  int *piVar2;
  uint uVar3;
  
  piVar2 = (int *)FUN_ffff00009077b004();
  _DAT_ffff000090843c28 = piVar2;
  if ((param_2 < 0x100) || (piVar2 == (int *)0x0)) {
    DAT_ffff000090843c30 = 1;
    FUN_ffff00009077de54
              (s__s__log_store_init_failed__ffff0000907d2775,s_LOG_STORE_LK_ffff0000907d7514);
    DAT_ffff000090843c30 = 0;
    return;
  }
  if (*piVar2 != 0x24013186) {
    DAT_ffff000090843c30 = 1;
    FUN_ffff00009077de54
              (s__s_sram_header_0x_p_is_not_match_ffff0000907e74e4,s_LOG_STORE_LK_ffff0000907d7514,
               piVar2);
    DAT_ffff000090843c30 = 0;
    FUN_ffff00009077eb88(_DAT_ffff000090843c28,0,0x100);
    piVar2 = _DAT_ffff000090843c28;
    *_DAT_ffff000090843c28 = 0x24013186;
  }
  iVar1 = piVar2[3];
  if (iVar1 == 0x5678ef90) {
    if (piVar2[0x10] == -0x3254cbee) goto LAB_ffff0000907191a0;
    DAT_ffff000090843c30 = 1;
    FUN_ffff00009077de54
              (s__s__BUFF_ERROR__sig_0x_x__buff_s_ffff0000907f41f6,s_LOG_STORE_LK_ffff0000907d7514,
               piVar2[0x10],piVar2[7],piVar2[0x12]);
    DAT_ffff000090843c30 = 0;
    piVar2 = _DAT_ffff000090843c28;
  }
  else {
    DAT_ffff000090843c30 = 1;
    FUN_ffff00009077de54
              (s__s_sram_dram_buff_0x_p__sig_0x_x_ffff0000907ebf12,s_LOG_STORE_LK_ffff0000907d7514,
               piVar2 + 3,iVar1,piVar2[4]);
    DAT_ffff000090843c30 = 0;
    FUN_ffff00009077eb88(_DAT_ffff000090843c28 + 3,0,0x34);
    piVar2 = _DAT_ffff000090843c28;
    _DAT_ffff000090843c28[3] = 0x5678ef90;
    piVar2[4] = 0x80;
  }
  FUN_ffff00009077eb88(piVar2 + 0x10,0,0x20);
  piVar2 = _DAT_ffff000090843c28;
  _DAT_ffff000090843c28[0x10] = -0x3254cbee;
LAB_ffff0000907191a0:
  uVar3 = piVar2[0x12] + piVar2[0x13];
  if ((uint)piVar2[7] <= uVar3) {
    DAT_ffff000090843c30 = 1;
    FUN_ffff00009077de54
              (s__s__buff_full_pl_size_0x_x__ffff0000907f422e,s_LOG_STORE_LK_ffff0000907d7514);
    piVar2 = _DAT_ffff000090843c28;
    DAT_ffff000090843c30 = 0;
    uVar3 = _DAT_ffff000090843c28[0x13] + _DAT_ffff000090843c28[0x12];
    _DAT_ffff000090843c28[4] = _DAT_ffff000090843c28[4] | 0x10;
  }
  piVar2[0x15] = uVar3;
  piVar2[0x17] = 0xc;
  FUN_ffff0000907c2c90(&PTR_LOOP_ffff0000908181d0);
  return;
}



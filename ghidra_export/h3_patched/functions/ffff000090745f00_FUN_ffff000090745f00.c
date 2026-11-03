// ffff000090745f00  size=184  FUN_ffff000090745f00

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090745f00(int param_1)

{
  undefined4 uVar1;
  undefined8 uVar2;
  
  if (param_1 == 7) {
    FUN_ffff00009077de54(s_Trigger_SYSTEM_RESET_with_SW_res_ffff0000907d7c1c);
    uVar2 = 0;
    uVar1 = 0x84000009;
  }
  else {
    if ((param_1 == 9) && ((_DAT_ffff00001c010024 & 0x7f0) == 0)) {
      _DAT_ffff00001c010024 = _DAT_ffff00001c010024 & 0xfffff80f | 0x60;
    }
    FUN_ffff00009077de54(s_Trigger_SYSTEM_RESET2_with_BL33_c_ffff0000907f4af9);
    uVar1 = 0xc4000012;
    uVar2 = 0x80000601;
  }
  FUN_ffff00009071ea6c(uVar1,uVar2,0,0,0,0,0,0,0);
  FUN_ffff00009077de54(s__s__spinning_forever____ffff0000907e4c70,s_mtk_wdt_reset_ffff0000907ec72b);
  do {
                    /* WARNING: Do nothing block with infinite loop */
  } while( true );
}



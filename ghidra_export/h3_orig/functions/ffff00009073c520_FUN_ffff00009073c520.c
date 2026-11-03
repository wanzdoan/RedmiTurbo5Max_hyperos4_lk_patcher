// ffff00009073c520  size=184  FUN_ffff00009073c520

void FUN_ffff00009073c520(code *param_1)

{
  int iVar1;
  uint uVar2;
  undefined8 uVar3;
  int local_2c;
  long local_28;
  
  local_28 = 0;
  local_2c = 0;
  iVar1 = FUN_ffff00009073bdf8(&local_28,&local_2c);
  uVar2 = 0;
  if ((iVar1 != 0) && (local_28 != 0)) {
    FUN_ffff00009077de54
              (s__s__In_dfd_mcu_dump_ffff0000907e2e13,s_save_dfd_mcu_data_ffff0000907cf882);
    if (local_2c < 1) {
      uVar2 = 0;
    }
    else {
      uVar3 = FUN_ffff00009077b06c(local_28);
      uVar2 = (*param_1)(uVar3,(long)local_2c);
      uVar2 = uVar2 & 1;
    }
    FUN_ffff00009077bb68(DAT_ffff000090839908,local_28);
  }
  FUN_ffff00009077de54
            (s__s__dfd_mcu_done__len___0x_x__da_ffff000090800acb,
             s_save_dfd_mcu_data_ffff0000907cf882,local_2c,uVar2);
  return;
}



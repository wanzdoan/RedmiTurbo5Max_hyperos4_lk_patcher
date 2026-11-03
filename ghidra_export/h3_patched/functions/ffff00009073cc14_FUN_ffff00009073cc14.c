// ffff00009073cc14  size=184  FUN_ffff00009073cc14

void FUN_ffff00009073cc14(code *param_1)

{
  int iVar1;
  uint uVar2;
  undefined8 uVar3;
  int local_2c;
  long local_28;
  
  local_28 = 0;
  local_2c = 0;
  iVar1 = FUN_ffff00009073ca9c(&local_28,&local_2c);
  uVar2 = 0;
  if ((iVar1 != 0) && (local_28 != 0)) {
    FUN_ffff00009077de54(s__s__start_ffff0000907e8f67,s_save_dfd_soc_data_ffff0000907d46bf);
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
            (s__s__dfd_soc_done__len___0x_x__da_ffff000090802644,
             s_save_dfd_soc_data_ffff0000907d46bf,local_2c,uVar2);
  return;
}



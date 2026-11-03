// ffff00009073ceac  size=184  FUN_ffff00009073ceac

void FUN_ffff00009073ceac(code *param_1)

{
  int iVar1;
  uint uVar2;
  undefined8 uVar3;
  int local_2c;
  long local_28;
  
  local_28 = 0;
  local_2c = 0;
  iVar1 = FUN_ffff00009073cd34(&local_28,&local_2c);
  uVar2 = 0;
  if ((iVar1 != 0) && (local_28 != 0)) {
    FUN_ffff00009077e11c(s__s__start_ffff0000907e90c2,s_save_dfd_soc_data_ffff0000907d46fc);
    if (local_2c < 1) {
      uVar2 = 0;
    }
    else {
      uVar3 = FUN_ffff00009077b334(local_28);
      uVar2 = (*param_1)(uVar3,(long)local_2c);
      uVar2 = uVar2 & 1;
    }
    FUN_ffff00009077be30(DAT_ffff000090839928,local_28);
  }
  FUN_ffff00009077e11c
            (s__s__dfd_soc_done__len___0x_x__da_ffff000090802848,
             s_save_dfd_soc_data_ffff0000907d46fc,local_2c,uVar2);
  return;
}



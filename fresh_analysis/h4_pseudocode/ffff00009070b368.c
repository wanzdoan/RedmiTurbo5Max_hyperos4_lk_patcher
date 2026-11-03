
void FUN_ffff00009070b368(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907f0cfa);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009071fbf4(&DAT_ffff0000907d3e3a);
    if (iVar1 == 0) {
LAB_ffff00009070b3f0:
      FUN_ffff000090709cdc(&DAT_ffff0000907df338);
      return;
    }
    pcVar2 = s_set_slot_a_failed__ffff0000907d7352;
  }
  else {
    iVar1 = FUN_ffff00009077f0ac(param_1,&DAT_ffff0000907d3e3d);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009071fbf4(&DAT_ffff0000907edab3);
      if (iVar1 == 0) goto LAB_ffff00009070b3f0;
      pcVar2 = s_set_slot_b_failed__ffff0000907e0abe;
    }
    else {
      pcVar2 = s_command_parameter_is_not_allowed_ffff0000907d0a3f;
    }
  }
  FUN_ffff000090709c9c(pcVar2);
  return;
}



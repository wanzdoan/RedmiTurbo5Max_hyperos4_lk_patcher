
void FUN_ffff00009070b290(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;

  iVar1 = FUN_ffff00009077f990(param_1,&DAT_ffff0000907e9856);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009071f714(&DAT_ffff0000907cbf12);
    if (iVar1 == 0) {
LAB_ffff00009070b318:
      FUN_ffff000090709dec(&DAT_ffff0000907d788e);
      return;
    }
    pcVar2 = s_set_slot_a_failed__ffff0000907cf598;
  }
  else {
    iVar1 = FUN_ffff00009077f990(param_1,&DAT_ffff0000907cbf15);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009071f714(&DAT_ffff0000907e664f);
      if (iVar1 == 0) goto LAB_ffff00009070b318;
      pcVar2 = s_set_slot_b_failed__ffff0000907d9048;
    }
    else {
      pcVar2 = s_command_parameter_is_not_allowed_ffff0000907c8a91;
    }
  }
  FUN_ffff000090709dac(pcVar2);
  return;
}

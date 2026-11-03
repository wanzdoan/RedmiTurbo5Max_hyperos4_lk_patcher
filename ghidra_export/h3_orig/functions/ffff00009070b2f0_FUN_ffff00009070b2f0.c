// ffff00009070b2f0  size=160  FUN_ffff00009070b2f0

void FUN_ffff00009070b2f0(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  
  iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907f0b89);
  if (iVar1 == 0) {
    iVar1 = FUN_ffff00009071fa6c(&DAT_ffff0000907d3dfd);
    if (iVar1 == 0) {
LAB_ffff00009070b378:
      FUN_ffff000090709d50(&DAT_ffff0000907df295);
      return;
    }
    pcVar2 = s_set_slot_a_failed__ffff0000907d72bc;
  }
  else {
    iVar1 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907d3e00);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009071fa6c(&DAT_ffff0000907ed942);
      if (iVar1 == 0) goto LAB_ffff00009070b378;
      pcVar2 = s_set_slot_b_failed__ffff0000907e0a1b;
    }
    else {
      pcVar2 = s_command_parameter_is_not_allowed_ffff0000907d0a28;
    }
  }
  FUN_ffff000090709d10(pcVar2);
  return;
}



// ffff00009074365c  size=132  FUN_ffff00009074365c

void FUN_ffff00009074365c(undefined8 *param_1,undefined4 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff00009071c57c();
  if (iVar1 == 0) {
    uVar2 = FUN_ffff000090719798(param_2,0x200000,0xc0000000,0,0,s_me_vcp_reserved_ffff0000907cf9d3)
    ;
  }
  else {
    uVar2 = FUN_ffff000090719f58
                      (param_2,0x200000,0x240000000,0x340000000,0,0,
                       s_me_vcp_reserved_ffff0000907cf9d3);
  }
  *param_1 = uVar2;
  return;
}



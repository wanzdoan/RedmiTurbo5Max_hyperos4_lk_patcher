
void FUN_ffff0000907438f4(undefined8 *param_1,undefined4 param_2)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff00009071c708();
  if (iVar1 == 0) {
    uVar2 = FUN_ffff0000907198dc(param_2,0x200000,0xc0000000,0,0,s_me_vcp_reserved_ffff0000907cf9f2)
    ;
  }
  else {
    uVar2 = FUN_ffff00009071a09c
                      (param_2,0x200000,0x240000000,0x340000000,0,0,
                       s_me_vcp_reserved_ffff0000907cf9f2);
  }
  *param_1 = uVar2;
  return;
}




undefined8 FUN_ffff000090738a4c(undefined8 *param_1,uint *param_2)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  iVar2 = FUN_ffff00009078aba4();
  iVar3 = FUN_ffff00009078abb8();
  uVar1 = iVar3 * 2 + 0x70ffffU & 0xffff0000;
  if (iVar3 == 0 || iVar2 == 0) {
    uVar1 = 0x2900000;
  }
  *param_2 = uVar1;
  iVar2 = FUN_ffff00009071c708();
  if (iVar2 == 0) {
    uVar4 = FUN_ffff0000907198dc(*param_2,0x8000000,0xc0000000,0,0,s_SCP_reserved_ffff0000907f48b4);
  }
  else {
    uVar4 = FUN_ffff00009071a09c
                      (*param_2,0x8000000,0x250000000,0x2a0000000,0,0,
                       s_SCP_reserved_ffff0000907f48b4);
  }
  *param_1 = uVar4;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  FUN_ffff00009071ebf4(0xc200040f,2,uVar4,*param_2,0,0,0,0,&local_50);
  return *param_1;
}



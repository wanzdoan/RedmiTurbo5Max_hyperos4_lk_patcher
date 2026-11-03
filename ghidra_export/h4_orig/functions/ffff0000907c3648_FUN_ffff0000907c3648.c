// ffff0000907c3648  size=284  FUN_ffff0000907c3648

int FUN_ffff0000907c3648(undefined8 param_1,undefined8 param_2,int param_3,int param_4)

{
  undefined8 uVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  undefined8 local_b0;
  ulong local_a8;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  local_98 = 0;
  local_b0 = 0;
  local_a8 = 0;
  FUN_ffff00009077ee50(&local_b0,0,0x70);
  local_70 = 0;
  uStack_68 = 0;
  local_60 = 0;
  local_a8 = local_a8 & 0xffffffff00000000;
  local_b0 = 0;
  iVar3 = FUN_ffff00009077658c(&local_b0,s_1_3_1_1_motley_ffff0000907ebf53,0x70);
  if (iVar3 == 0) {
    iVar3 = 0;
    uVar1 = param_1;
    iVar2 = param_3;
    iVar5 = iVar3;
    if (param_3 != 0) {
      do {
        local_a8._0_4_ = iVar2;
        local_b0 = uVar1;
        local_90 = CONCAT44(local_90._4_4_,param_4);
        local_98 = param_2;
        iVar4 = FUN_ffff000090776678(&local_b0,0);
        iVar3 = iVar4;
        if (iVar4 + 4U < 2) {
LAB_ffff0000907c3730:
          FUN_ffff00009077e11c(s_Z_DATA_ERROR_or_Z_MEM_ERROR_ffff0000907ed912);
          break;
        }
        if (iVar4 == 2) {
          iVar3 = -3;
          goto LAB_ffff0000907c3730;
        }
        iVar3 = (param_4 - (int)local_90) + iVar5;
        uVar1 = local_b0;
        iVar2 = (int)local_a8;
        iVar5 = iVar3;
      } while (((int)local_90 == 0) || (uVar1 = param_1, iVar2 = param_3, iVar4 != 1));
      FUN_ffff0000907785bc(&local_b0);
    }
  }
  return iVar3;
}



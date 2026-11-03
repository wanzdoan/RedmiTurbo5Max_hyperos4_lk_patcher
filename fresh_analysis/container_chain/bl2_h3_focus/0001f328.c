// File offset entry: 0x1f328

void FUN_ffff0000b801f328(long param_1,long param_2,int param_3)

{
  int iVar1;
  ulong uVar2;
  undefined *puVar3;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_18;
  
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_78 = 0;
  if (((param_1 == 0) || (param_2 == 0)) || (param_3 != 0x48)) {
    FUN_ffff0000b804beb0(s__Part__illegal_ptr__p_size__d_ffff0000b8087114,param_1);
    return;
  }
  uVar2 = FUN_ffff0000b800fb60();
  FUN_ffff0000b804cbe4(&local_78,0,0x48);
  if ((uVar2 & 1) == 0) {
    iVar1 = FUN_ffff0000b804d2c0(&local_78,0x47,&DAT_ffff0000b808dc9f,param_1,&DAT_ffff0000b8086222)
    ;
    if (iVar1 < 0) goto LAB_ffff0000b801f498;
    iVar1 = FUN_ffff0000b801f4c0(&local_78);
    if (iVar1 != 0) {
      local_18 = 0;
      FUN_ffff0000b801f5b0(&local_78,&local_18);
      FUN_ffff0000b804cbe4(&local_78,0,0x48);
      puVar3 = &DAT_ffff0000b8089964;
      goto LAB_ffff0000b801f420;
    }
LAB_ffff0000b801f440:
    FUN_ffff0000b804cbe4(&local_78,0,0x48);
    iVar1 = FUN_ffff0000b804d2c0(&local_78,0x47,&DAT_ffff0000b808b4ba,param_1);
    if (iVar1 < 0) goto LAB_ffff0000b801f498;
  }
  else {
    puVar3 = (undefined *)FUN_ffff0000b800f6b8();
LAB_ffff0000b801f420:
    iVar1 = FUN_ffff0000b804d2c0(&local_78,0x47,&DAT_ffff0000b808dc9f,param_1,puVar3);
    if (iVar1 < 0) goto LAB_ffff0000b801f498;
    iVar1 = FUN_ffff0000b801f4c0(&local_78);
    if (iVar1 == 0) goto LAB_ffff0000b801f440;
  }
  FUN_ffff0000b804cbe4(param_2,0,0x48);
  iVar1 = FUN_ffff0000b804d2c0(param_2,0x47,&DAT_ffff0000b808b4ba,&local_78);
  if (-1 < iVar1) {
    return;
  }
LAB_ffff0000b801f498:
  FUN_ffff0000b804beb0(s_Fail_to_revise_part_name___d__ffff0000b809c641,iVar1);
  return;
}




void FUN_ffff0000be028090(long param_1,long param_2,int param_3)

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
    FUN_ffff0000be05fd98(s__Part__illegal_ptr__p_size__d_ffff0000be0a6d90,param_1);
    return;
  }
  uVar2 = FUN_ffff0000be01e518();
  FUN_ffff0000be060ae0(&local_78,0,0x48);
  if ((uVar2 & 1) == 0) {
    iVar1 = FUN_ffff0000be0611dc(&local_78,0x47,&DAT_ffff0000be0af57f,param_1,&DAT_ffff0000be0a57f3)
    ;
    if (iVar1 < 0) goto LAB_ffff0000be028200;
    iVar1 = FUN_ffff0000be028228(&local_78);
    if (iVar1 != 0) {
      local_18 = 0;
      FUN_ffff0000be028318(&local_78,&local_18);
      FUN_ffff0000be060ae0(&local_78,0,0x48);
      puVar3 = &DAT_ffff0000be0aa1ff;
      goto LAB_ffff0000be028188;
    }
LAB_ffff0000be0281a8:
    FUN_ffff0000be060ae0(&local_78,0,0x48);
    iVar1 = FUN_ffff0000be0611dc(&local_78,0x47,&DAT_ffff0000be0ac42b,param_1);
    if (iVar1 < 0) goto LAB_ffff0000be028200;
  }
  else {
    puVar3 = (undefined *)FUN_ffff0000be01e138();
LAB_ffff0000be028188:
    iVar1 = FUN_ffff0000be0611dc(&local_78,0x47,&DAT_ffff0000be0af57f,param_1,puVar3);
    if (iVar1 < 0) goto LAB_ffff0000be028200;
    iVar1 = FUN_ffff0000be028228(&local_78);
    if (iVar1 == 0) goto LAB_ffff0000be0281a8;
  }
  FUN_ffff0000be060ae0(param_2,0,0x48);
  iVar1 = FUN_ffff0000be0611dc(param_2,0x47,&DAT_ffff0000be0ac42b,&local_78);
  if (-1 < iVar1) {
    return;
  }
LAB_ffff0000be028200:
  FUN_ffff0000be05fd98(s_Fail_to_revise_part_name___d__ffff0000be0c1b71,iVar1);
  return;
}



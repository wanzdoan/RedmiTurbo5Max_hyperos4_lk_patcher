
undefined8 FUN_ffff0000be096810(long param_1,char *param_2,int param_3)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  long lVar5;
  char *pcVar6;
  int local_44;
  
  if (*param_2 == '/') {
    uVar3 = FUN_ffff0000be09789c(*(undefined8 *)(param_1 + 0x10),param_2);
    return uVar3;
  }
  pcVar4 = (char *)thunk_FUN_ffff0000be060748(param_2,0x2f);
  pcVar6 = param_2 + param_3;
  if (pcVar4 != (char *)0x0) {
    pcVar6 = pcVar4;
  }
  uVar3 = FUN_ffff0000be0979b8(*(undefined8 *)(param_1 + 0x10),s__aliases_ffff0000be0ade0c);
  uVar3 = FUN_ffff0000be0974ec(uVar3,param_2,(int)pcVar6 - (int)param_2);
  local_44 = 0;
  lVar5 = FUN_ffff0000be09769c(uVar3,&local_44);
  iVar1 = local_44;
  if ((lVar5 == 0) || (local_44 == 0)) {
    FUN_ffff0000be09664c
              (s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_node_by_path_len_ffff0000be0a66cc);
    pcVar6 = s_Failed_to_find_valid_alias__s_ffff0000be0ab0cd;
  }
  else {
    iVar2 = FUN_ffff0000be061040(lVar5,(long)local_44);
    if ((iVar1 + -1 == iVar2) && (iVar2 != 0)) {
      uVar3 = FUN_ffff0000be09789c(*(undefined8 *)(param_1 + 0x10),lVar5);
      uVar3 = FUN_ffff0000be09789c(uVar3,pcVar6,(int)(param_2 + param_3) - (int)pcVar6);
      return uVar3;
    }
    FUN_ffff0000be09664c
              (s_ERROR___s____ffff0000be0bcb11,s_ufdt_get_node_by_path_len_ffff0000be0a66cc);
    pcVar6 = s_Invalid_alias_for__s_ffff0000be0bcb1e;
  }
  FUN_ffff0000be09664c(pcVar6,param_2);
  return 0;
}



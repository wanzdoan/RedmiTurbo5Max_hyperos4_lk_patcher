// ffff00009076fdc4  size=332  FUN_ffff00009076fdc4

undefined8 FUN_ffff00009076fdc4(long param_1,char *param_2,int param_3)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  char *pcVar4;
  long lVar5;
  char *pcVar6;
  int local_44;
  
  if (*param_2 == '/') {
    uVar3 = FUN_ffff000090770e50(*(undefined8 *)(param_1 + 0x10),param_2);
    return uVar3;
  }
  pcVar4 = (char *)thunk_FUN_ffff00009077e7f0(param_2,0x2f);
  pcVar6 = param_2 + param_3;
  if (pcVar4 != (char *)0x0) {
    pcVar6 = pcVar4;
  }
  uVar3 = FUN_ffff000090770f6c(*(undefined8 *)(param_1 + 0x10),s__aliases_ffff0000907deab2);
  uVar3 = FUN_ffff000090770aa0(uVar3,param_2,(int)pcVar6 - (int)param_2);
  local_44 = 0;
  lVar5 = FUN_ffff000090770c50(uVar3,&local_44);
  iVar1 = local_44;
  if ((lVar5 == 0) || (local_44 == 0)) {
    FUN_ffff00009076fc00
              (s_ERROR___s____ffff0000907f3517,s_ufdt_get_node_by_path_len_ffff0000907d36ff);
    pcVar6 = s_Failed_to_find_valid_alias__s_ffff0000907d9caf;
  }
  else {
    iVar2 = FUN_ffff00009077f16c(lVar5,(long)local_44);
    if ((iVar1 + -1 == iVar2) && (iVar2 != 0)) {
      uVar3 = FUN_ffff000090770e50(*(undefined8 *)(param_1 + 0x10),lVar5);
      uVar3 = FUN_ffff000090770e50(uVar3,pcVar6,(int)(param_2 + param_3) - (int)pcVar6);
      return uVar3;
    }
    FUN_ffff00009076fc00
              (s_ERROR___s____ffff0000907f3517,s_ufdt_get_node_by_path_len_ffff0000907d36ff);
    pcVar6 = s_Invalid_alias_for__s_ffff0000907f3524;
  }
  FUN_ffff00009076fc00(pcVar6,param_2);
  return 0;
}



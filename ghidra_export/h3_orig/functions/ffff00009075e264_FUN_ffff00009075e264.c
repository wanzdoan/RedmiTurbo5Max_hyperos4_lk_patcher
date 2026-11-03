// ffff00009075e264  size=232  FUN_ffff00009075e264

void FUN_ffff00009075e264(undefined8 param_1)

{
  char cVar1;
  int iVar2;
  undefined4 local_24;
  
  local_24 = 0x1000000;
  cVar1 = thunk_FUN_ffff00009074af94(0x1e);
  FUN_ffff00009077de54(s__s__seg___0x_x_ffff00009080117a,s_set_pf_ctrl_ffff0000907f3288,cVar1);
  if ((cVar1 == '\x13') || (cVar1 == '\x05')) {
    iVar2 = FUN_ffff00009076d75c(param_1,s__pf_ctrl_ffff0000907d68cf);
    if (iVar2 < 0) {
      FUN_ffff00009077de54(s_Node__pf_ctrl__not_found_ffff0000907fbf04);
      return;
    }
    iVar2 = FUN_ffff00009076e40c(param_1,iVar2,s_version_ffff0000907f79b4,&local_24,4);
    if (iVar2 < 0) {
      FUN_ffff00009077de54
                (s_Failed_to_set_property___s____d_ffff0000907e1b41,s_version_ffff0000907f79b4,iVar2
                );
    }
    else {
      FUN_ffff00009077de54
                (s_Property___s__set_successfully_ffff000090802a5f,s_version_ffff0000907f79b4);
    }
  }
  return;
}



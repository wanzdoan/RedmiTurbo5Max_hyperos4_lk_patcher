// ffff00009075e4fc  size=232  FUN_ffff00009075e4fc

void FUN_ffff00009075e4fc(undefined8 param_1)

{
  char cVar1;
  int iVar2;
  undefined4 local_24;
  
  local_24 = 0x1000000;
  cVar1 = thunk_FUN_ffff00009074b22c(0x1e);
  FUN_ffff00009077e11c(s__s__seg___0x_x_ffff000090801356,s_set_pf_ctrl_ffff0000907f3414,cVar1);
  if ((cVar1 == '\x13') || (cVar1 == '\x05')) {
    iVar2 = FUN_ffff00009076da24(param_1,s__pf_ctrl_ffff0000907d6965);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c(s_Node__pf_ctrl__not_found_ffff0000907fc0a0);
      return;
    }
    iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,s_version_ffff0000907f7b3e,&local_24,4);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c
                (s_Failed_to_set_property___s____d_ffff0000907e1c12,s_version_ffff0000907f7b3e,iVar2
                );
    }
    else {
      FUN_ffff00009077e11c
                (s_Property___s__set_successfully_ffff000090802c63,s_version_ffff0000907f7b3e);
    }
  }
  return;
}



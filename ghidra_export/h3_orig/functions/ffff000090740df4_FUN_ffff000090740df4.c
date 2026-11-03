// ffff000090740df4  size=288  FUN_ffff000090740df4

void FUN_ffff000090740df4(undefined8 param_1,ulong param_2,uint param_3)

{
  ushort uVar1;
  int iVar2;
  int iVar3;
  undefined1 local_4c [4];
  undefined2 local_48 [2];
  short local_44 [2];
  
  uVar1 = *(ushort *)(&DAT_ffff000090804980 + (param_2 & 0xffffffff) * 2);
  local_4c[0] = (undefined1)param_3;
  FUN_ffff00009074021c(param_1,uVar1,local_4c,1);
  local_4c[0] = (undefined1)(param_3 >> 8);
  FUN_ffff00009074021c(param_1,uVar1 + 1,local_4c,1);
  if ((param_3 & 0xffff) == 0) {
    iVar3 = 0;
    local_44[0] = 0;
    local_48[0] = 0;
    do {
      iVar2 = FUN_ffff0000907401d4(param_1,(uint)uVar1,local_44,2);
      if (iVar2 < 0) {
        FUN_ffff00009077de54
                  (s___s___read_fail__addr___0x_x__re_ffff0000907d47dc,
                   s_mt6316_key_lock_check_ffff0000907f4a38,uVar1,iVar2);
LAB_ffff000090740eb4:
        iVar2 = 0;
      }
      else {
        iVar2 = iVar3;
        if (local_44[0] != 0) {
          FUN_ffff00009077de54
                    (s___s__lock_fail__addr___0x_x__rda_ffff0000907e638f,
                     s_mt6316_key_lock_check_ffff0000907f4a38,uVar1);
          local_48[0] = 0;
          FUN_ffff00009074021c(param_1,uVar1,local_48,2);
          goto LAB_ffff000090740eb4;
        }
      }
      iVar3 = iVar2 + 1;
    } while (iVar2 < 1);
  }
  return;
}



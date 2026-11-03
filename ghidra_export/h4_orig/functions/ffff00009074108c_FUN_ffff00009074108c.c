// ffff00009074108c  size=288  FUN_ffff00009074108c

void FUN_ffff00009074108c(undefined8 param_1,ulong param_2,uint param_3)

{
  ushort uVar1;
  int iVar2;
  int iVar3;
  undefined1 local_4c [4];
  undefined2 local_48 [2];
  short local_44 [2];
  
  uVar1 = *(ushort *)(&DAT_ffff000090804ba8 + (param_2 & 0xffffffff) * 2);
  local_4c[0] = (undefined1)param_3;
  FUN_ffff0000907404b4(param_1,uVar1,local_4c,1);
  local_4c[0] = (undefined1)(param_3 >> 8);
  FUN_ffff0000907404b4(param_1,uVar1 + 1,local_4c,1);
  if ((param_3 & 0xffff) == 0) {
    iVar3 = 0;
    local_44[0] = 0;
    local_48[0] = 0;
    do {
      iVar2 = FUN_ffff00009074046c(param_1,(uint)uVar1,local_44,2);
      if (iVar2 < 0) {
        FUN_ffff00009077e11c
                  (s___s___read_fail__addr___0x_x__re_ffff0000907d4819,
                   s_mt6316_key_lock_check_ffff0000907f4ba5,uVar1,iVar2);
LAB_ffff00009074114c:
        iVar2 = 0;
      }
      else {
        iVar2 = iVar3;
        if (local_44[0] != 0) {
          FUN_ffff00009077e11c
                    (s___s__lock_fail__addr___0x_x__rda_ffff0000907e64a3,
                     s_mt6316_key_lock_check_ffff0000907f4ba5,uVar1);
          local_48[0] = 0;
          FUN_ffff0000907404b4(param_1,uVar1,local_48,2);
          goto LAB_ffff00009074114c;
        }
      }
      iVar3 = iVar2 + 1;
    } while (iVar2 < 1);
  }
  return;
}




undefined8 FUN_ffff0000907c5670(long param_1,long param_2,undefined4 *param_3)

{
  int iVar1;
  uint uVar2;
  undefined8 uVar3;
  undefined1 *in_x6;
  undefined4 *in_x7;
  ulong uVar4;
  
  uVar3 = 2;
  if ((((in_x7 != (undefined4 *)0x0) && (param_3 != (undefined4 *)0x0)) && (param_1 != 0)) &&
     ((param_2 != 0 && (in_x6 != (undefined1 *)0x0)))) {
    uVar3 = FUN_ffff00009077f250(s_recovery_ffff0000907e5ba4);
    iVar1 = FUN_ffff00009077eae8(param_2,s_recovery_ffff0000907e5ba4,uVar3);
    if (iVar1 == 0) {
      *in_x7 = 1;
      uVar2 = FUN_ffff0000907c1070(*param_3);
      *in_x6 = 0;
      if ((uVar2 & 0xfffffff8) == 0x1000) {
        uVar3 = FUN_ffff00009077eae8(&DAT_ffff0000908384e0,param_3 + 2,0x200);
        if ((int)uVar3 == 0) {
          *in_x6 = 1;
          return uVar3;
        }
        FUN_ffff00009077e11c(s__AVB__recovery_pubk_hash_in_lk_p_ffff0000907f78bb);
        uVar4 = 0;
        do {
          FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,(&DAT_ffff0000908384e0)[uVar4]);
          uVar4 = uVar4 + 1;
          if ((uVar4 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar4 != 0x200);
        FUN_ffff00009077e11c(s__AVB__pubk_hash_in__s_partition___ffff0000907ea4f1,param_2);
        uVar4 = 0;
        do {
          FUN_ffff00009077e11c(s_0x_x_ffff0000907fe651,*(undefined1 *)((long)(param_3 + 2) + uVar4))
          ;
          uVar4 = uVar4 + 1;
          if ((uVar4 & 0xf) == 0) {
            FUN_ffff00009077e11c(&DAT_ffff0000907f836a);
          }
        } while (uVar4 != 0x200);
      }
      else {
        FUN_ffff00009077e11c(s__AVB___s__invalid_pubk_size_ffff0000907ea4d4,param_2);
      }
      uVar3 = 0;
    }
    else {
      uVar3 = 3;
    }
  }
  return uVar3;
}



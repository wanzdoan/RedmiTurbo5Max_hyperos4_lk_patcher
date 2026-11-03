
undefined8 FUN_ffff0000b800e938(long param_1)

{
  int iVar1;
  ulong uVar2;
  undefined4 *puVar3;
  
  DAT_ffff0000b811ae60 = FUN_ffff0000b800eda4();
  if (DAT_ffff0000b811ae60 < 3) {
    if (DAT_ffff0000b811ae60 == 0) {
      return 0;
    }
  }
  else {
    FUN_ffff0000b804c33c
              (s__s_fail__gm_no__d_>_MAX__d_ffff0000b808b51d,s_mtk_battery_init_ffff0000b809c245,
               DAT_ffff0000b811ae60,2);
    DAT_ffff0000b811ae60 = 2;
  }
  uVar2 = 0;
  puVar3 = &DAT_ffff0000b811ae70;
  do {
    if ((&DAT_ffff0000b811ae64)[uVar2] == '\0') {
      *(undefined8 *)(puVar3 + -2) = 0x84d000000000;
      *puVar3 = 500;
      (&DAT_ffff0000b811ae64)[uVar2] = 1;
      if (param_1 == 0) {
        FUN_ffff0000b804c33c
                  (s__bat__fdt__u_null__fall_back_to_d_ffff0000b8088ac9,uVar2 & 0xffffffff);
      }
      else {
        iVar1 = FUN_ffff0000b80470b4(param_1);
        if (iVar1 != 0) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000b804b744(s_Bad_DTB_header__ffff0000b8086070);
        }
        FUN_ffff0000b800ea4c(param_1,uVar2 & 0xffffffff);
      }
    }
    uVar2 = uVar2 + 1;
    puVar3 = puVar3 + 3;
  } while (uVar2 < DAT_ffff0000b811ae60);
  return 0;
}



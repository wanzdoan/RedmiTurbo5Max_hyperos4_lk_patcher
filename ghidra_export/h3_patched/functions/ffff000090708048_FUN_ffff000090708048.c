// ffff000090708048  size=572  FUN_ffff000090708048

void FUN_ffff000090708048(long param_1)

{
  char *pcVar1;
  ulong uVar2;
  undefined8 uVar3;
  uint uVar4;
  undefined4 uVar5;
  int iVar6;
  undefined4 uVar7;
  ulong uVar8;
  long *plVar9;
  ulong uVar10;
  
  uVar2 = esr_el1;
  uVar8 = uVar2 >> 0x1a & 0x3f;
  uVar4 = (uint)uVar2;
  iVar6 = (int)uVar8;
  switch(iVar6) {
  case 7:
    FUN_ffff00009070866c();
    return;
  default:
    FUN_ffff00009077de54(s_unhandled_synchronous_exception_ffff0000907f7843);
    break;
  case 0xd:
    FUN_ffff00009077de54
              (s_branch_target_exception__BTYPE_0_ffff0000907f90e8,uVar2 & 3,
               *(undefined8 *)(param_1 + 0x100));
    break;
  case 0x11:
  case 0x15:
    FUN_ffff000090708030(param_1,iVar6 == 0x15);
    return;
  case 0x1c:
    uVar7 = 0x49;
    if ((uVar2 & 2) != 0) {
      uVar7 = 0x44;
    }
    uVar5 = 0x41;
    if ((uVar2 & 1) != 0) {
      uVar5 = 0x42;
    }
    FUN_ffff00009077de54
              (s_PAC_auth_failure_target_exceptio_ffff00009080018f,uVar7,uVar5,
               *(undefined8 *)(param_1 + 0x100));
    break;
  case 0x20:
  case 0x21:
    FUN_ffff00009077de54
              (s_instruction_abort__PC_at_0x_llx_ffff0000907d7266,*(undefined8 *)(param_1 + 0x100));
    uVar10 = (ulong)(uVar4 & 0x3f);
    goto LAB_ffff00009070812c;
  case 0x24:
  case 0x25:
    if (PTR_DAT_ffff000090839840 < PTR_DAT_ffff000090839838) {
      plVar9 = (long *)PTR_DAT_ffff000090839840;
      do {
        if (*plVar9 == *(long *)(param_1 + 0x100)) {
          *(long *)(param_1 + 0x100) = plVar9[1];
          return;
        }
        plVar9 = plVar9 + 2;
      } while (plVar9 < PTR_DAT_ffff000090839838);
    }
    uVar10 = uVar2 & 0x3f;
    uVar3 = far_el1;
    pcVar1 = &DAT_ffff0000907e23b3;
    if ((uVar2 & 0x40) != 0) {
      pcVar1 = s_Write_ffff0000907ea474;
    }
    FUN_ffff00009077de54
              (s_data_fault___s_access_from_PC_0x_ffff0000907e3ffe,pcVar1,
               *(undefined8 *)(param_1 + 0x100),uVar3,uVar4 & 0x1ffffff,uVar10);
LAB_ffff00009070812c:
    FUN_ffff000090708284(uVar10);
    break;
  case 0x3c:
    FUN_ffff00009077de54
              (s_BRK__0x_04lx_instruction__PC_at_0_ffff0000907e4047,uVar2 & 0xffff,
               *(undefined8 *)(param_1 + 0x100));
  }
  FUN_ffff00009077de54
            (s_ESR_0x_x__ec_0x_x__il_0x_x__iss_0_ffff0000907e725a,uVar2,uVar8,uVar4 & 0x2000000,
             uVar4 & 0x1ffffff);
  FUN_ffff0000907083ec(param_1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(&DAT_ffff0000907e8b04);
}



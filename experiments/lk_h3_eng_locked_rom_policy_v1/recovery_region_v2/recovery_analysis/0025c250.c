
/* MiScreenRecoveryUI::StartProgress(float, float) */

void __thiscall
MiScreenRecoveryUI::StartProgress(MiScreenRecoveryUI *this,float param_1,float param_2)

{
  long lVar1;
  int iVar2;
  float fVar3;
  undefined4 uVar4;
  timeval local_58;
  long local_48;

  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  std::mutex::lock();
  uVar4 = 0;
  *(undefined4 *)(this + 0xbd0) = 3;
  *(undefined8 *)(this + 0xbb0) = 0;
  *(MiScreenRecoveryUI **)(this + 0xc08) = this + 0xe78;
  *(undefined8 *)(this + 0xbdc) = 0x400000470;
  *(undefined8 *)(this + 0xbfc) = 0x4000000dc;
  *(MiScreenRecoveryUI **)(this + 0xc10) = this + 0xe80;
  *(undefined8 *)(this + 0xbc8) = 0;
  *(undefined8 *)(this + 0xbc0) = 0;
  *(undefined8 *)(this + 0xbf0) = 0;
  *(undefined8 *)(this + 0xbe8) = 0;
  *(undefined8 *)(this + 0xc1c) = 0x400000000;
  *(undefined8 *)(this + 0xc30) = 0;
  *(undefined8 *)(this + 0xc28) = 0;
  fVar3 = 0.0;
  if ((0.0 <= param_1) && (fVar3 = param_1, 1.0 < param_1)) {
    fVar3 = 1.0;
  }
  *(float *)(this + 3000) = fVar3;
  local_58.tv_sec = 0;
  local_58.tv_usec = 0;
  iVar2 = gettimeofday(&local_58,(__timezone_ptr_t)0x0);
  *(double *)(this + 0xbc8) = (double)param_2;
  fVar3 = *(float *)(this + 0xbb4);
  *(double *)(this + 0xbc0) = (double)local_58.tv_usec / 1000000.0 + (double)local_58.tv_sec;
  if ((((param_2 == 0.0) && (fVar3 <= *(float *)(this + 0xbb0))) ||
      (*(float *)(this + 0xbb0) = fVar3, fVar3 < 0.0)) || (uVar4 = 0x3f800000, 1.0 < fVar3)) {
    *(undefined4 *)(this + 0xbb0) = uVar4;
  }
  this[0xba0] = (MiScreenRecoveryUI)0x0;
  this[0xd18] = (MiScreenRecoveryUI)0x1;
  if (*(long *)(lVar1 + 0x28) == local_48) {
    std::mutex::unlock();
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(iVar2);
}
